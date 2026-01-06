package io.github.team9.escapefromuni;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class PositiveEventGuide {
    private final Main game;
    private final Vector2 roncookePos;
    private final Vector2 langwithPos;
    private final float arrivalRadius;
    private int stage = 0;
    private boolean active = false;
    private boolean completed = false;
    private boolean counted = false;
    private final BitmapFont font;
    private final Texture[] arrows;
    private final TiledMapTileLayer collisionLayer;
    private final int tileW, tileH, mapWallsId;

    public PositiveEventGuide(Main game, Vector2 roncookePos, Vector2 langwithPos, float arrivalRadius, BitmapFont font, TiledMapTileLayer collisionLayer, int mapWallsId) {
        this.game = game;
        this.roncookePos = roncookePos;
        this.langwithPos = langwithPos;
        this.arrivalRadius = arrivalRadius;
        this.font = game.menuFont; // reuse the menu font for guide text
        arrows = new Texture[4];
        arrows[0] = new Texture("Guide/00_0.png"); // right
        arrows[1] = new Texture("Guide/00_90.png"); // up
        arrows[2] = new Texture("Guide/00_180.png"); // left
        arrows[3] = new Texture("Guide/00_270.png"); // down
        this.collisionLayer = collisionLayer;
        this.mapWallsId = mapWallsId;
        tileW = collisionLayer.getTileWidth();
        tileH = collisionLayer.getTileHeight();
    }


    public void start() {
        if (!completed) active = true;
    }

    public void stop() {
        active = false;
    }

    public boolean isActive() {
        return active && !completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    /**
     * Call each frame with player center coords.
     */
    public void update(float playerX, float playerY) {
        if (!active || completed) return;
        Vector2 target = (stage == 0) ? roncookePos : langwithPos;
        float dx = target.x - playerX;
        float dy = target.y - playerY;
        float dist2 = dx * dx + dy * dy;
        if (dist2 <= arrivalRadius * arrivalRadius) {
            if (stage == 0) {
                // reached RonCooke -> advance to Langwith
                stage = 1;
            } else {
                // finished both steps
                completed = true;
                active = false;
                if (!counted) {
                    counted = true;
                    game.foundPositiveEvents += 1;
                }
            }
        }
    }

    /**
     * Draw a simple directional hint overlay.
     * Call after setting SpriteBatch projection (game.batch.begin()).
     */
    public void render(SpriteBatch batch, Camera camera, float playerX, float playerY) {
        if (!isActive()) return;
        Vector2 target = (stage == 0) ? roncookePos : langwithPos;
        float dist = (float) Math.sqrt((target.x - playerX)*(target.x - playerX) + (target.y - playerY)*(target.y - playerY));
        String label = (stage == 0) ? "Head to RonCooke" : "Return to Langwith";
        String hint = String.format("%s  (%.0fm)", label, dist);
        // Draw text under score in UI space
        float textX = camera.position.x - camera.viewportWidth / 2f + 20f;
        float textY = camera.position.y + camera.viewportHeight / 2f - 60f;
        font.setColor(Color.YELLOW);
        font.getData().setScale(0.5f);
        font.draw(batch, hint, textX, textY);
        // Arrow next to player (unchanged)
        float arrowX = playerX - 130f;
        float arrowY = playerY + 20f;
        // Compute path and get direction to next step
        float dx = target.x - playerX;
        float dy = target.y - playerY;
        int pTileX = (int)(playerX / tileW);
        int pTileY = (int)(playerY / tileH);
        int tTileX = (int)(target.x / tileW);
        int tTileY = (int)(target.y / tileH);
        List<Vector2> path = findPath(pTileX, pTileY, tTileX, tTileY);
        if (path.size() > 1) {
            Vector2 next = path.get(1);
            float localDx = next.x * tileW + tileW / 2f - playerX;
            float localDy = next.y * tileH + tileH / 2f - playerY;
            float angleDeg = (float) Math.toDegrees(Math.atan2(localDy, localDx));
            angleDeg = (angleDeg % 360 + 360) % 360; // normalize to 0-360
            float[] dirs = {0, 90, 180, 270};
            int index = 0;
            float minDiff = Math.abs(angleDeg - dirs[0]);
            for (int i = 1; i < 4; i++) {
                float diff = Math.abs(angleDeg - dirs[i]);
                if (diff < minDiff) {
                    minDiff = diff;
                    index = i;
                }
            }
            batch.draw(arrows[index], arrowX + 120f, arrowY - 10f, 20f, 20f);
        } else {
            // Fallback: point to target
            float angleDeg = (float) Math.toDegrees(Math.atan2(dy, dx));
            angleDeg = (angleDeg % 360 + 360) % 360;
            float[] dirs = {0, 90, 180, 270};
            int index = 0;
            float minDiff = Math.abs(angleDeg - dirs[0]);
            for (int i = 1; i < 4; i++) {
                float diff = Math.abs(angleDeg - dirs[i]);
                if (diff < minDiff) {
                    minDiff = diff;
                    index = i;
                }
            }
            batch.draw(arrows[index], arrowX + 120f, arrowY - 10f, 20f, 20f);
        }
        // restore scale for other UI usage
        font.getData().setScale(0.8f);
    }

    private static class Node {
        int x, y;
        float g, h, f;
        Node parent;

        Node(int x, int y, float g, float h, Node parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.parent = parent;
        }
    }

    private List<Vector2> findPath(int startX, int startY, int goalX, int goalY) {
        PriorityQueue<Node> open = new PriorityQueue<>((a, b) -> Float.compare(a.f, b.f));
        Set<String> closed = new HashSet<>();
        open.add(new Node(startX, startY, 0, heuristic(startX, startY, goalX, goalY), null));
        while (!open.isEmpty()) {
            Node current = open.poll();
            if (current.x == goalX && current.y == goalY) {
                return reconstructPath(current);
            }
            String key = current.x + "," + current.y;
            if (closed.contains(key)) continue;
            closed.add(key);
            for (int[] dir : new int[][]{{0,1},{1,0},{0,-1},{-1,0}}) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (nx < 0 || ny < 0 || nx >= collisionLayer.getWidth() || ny >= collisionLayer.getHeight()) continue;
                if (collisionLayer.getCell(nx, ny) != null && collisionLayer.getCell(nx, ny).getTile().getId() == mapWallsId) continue;
                String nkey = nx + "," + ny;
                if (closed.contains(nkey)) continue;
                float g = current.g + 1;
                float h = heuristic(nx, ny, goalX, goalY);
                open.add(new Node(nx, ny, g, h, current));
            }
        }
        return new ArrayList<>();
    }

    private float heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private List<Vector2> reconstructPath(Node node) {
        List<Vector2> path = new ArrayList<>();
        while (node != null) {
            path.add(0, new Vector2(node.x, node.y));
            node = node.parent;
        }
        return path;
    }

    public void dispose() {
        for (Texture t : arrows) {
            t.dispose();
        }
    }
}
