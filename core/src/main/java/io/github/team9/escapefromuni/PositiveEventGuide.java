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
    private boolean started = false;
    private final BitmapFont font;
    private final Texture[] arrows;
    private final TiledMapTileLayer collisionLayer;
    private final int tileW, tileH, mapWallsId;

    /**
     * Constructs a new PositiveEventGuide.
     * @param roncookePos the position of RonCooke building
     * @param langwithPos the position of Langwith building
     * @param arrivalRadius Radius that checks for player arrival
     * @param collisionLayer the tiled map collision layer for pathfinding
     * @param mapWallsId the tile ID for walls in the collision layer
     */
    public PositiveEventGuide(Main game, Vector2 roncookePos, Vector2 langwithPos, float arrivalRadius, BitmapFont font, TiledMapTileLayer collisionLayer, int mapWallsId) {
        this.game = game;
        this.roncookePos = roncookePos;
        this.langwithPos = langwithPos;
        this.arrivalRadius = arrivalRadius;
        this.font = game.menuFont; // reuse the menu font for guide text
        arrows = new Texture[4];
        // Load textures lazily in render to avoid issues in headless tests
        this.collisionLayer = collisionLayer;
        this.mapWallsId = mapWallsId;
        tileW = collisionLayer.getTileWidth();
        tileH = collisionLayer.getTileHeight();
    }


    /**
     * Starts the guide if not already completed.
     * Increments the positive events counter
     */
    public void start() {
        if (!completed) {
            active = true;
            if (!started) {
                game.foundPositiveEvents++;
                started = true;
            }
        }
    }

    /**
     * Stops the guide.
     */
    public void stop() {
        active = false;
    }

    /**
     * Checks if the guide is currently active
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return active && !completed;
    }

    /**
     * Checks if the guide has been completed.
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Gets the current stage of the guide.
     * @return 0 for RonCooke stage, 1 for Langwith stage
     */
    public int getStage() {
        return stage;
    }

    /**
     * Updates the guide's state based on player position.
     * Advances stages or completes the guide when the player reaches targets.
     * @param playerX the player's x-coordinate
     * @param playerY the player's y-coordinate
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
     * Renders the guide's directional arrows on the map.
     * Draws arrows along the path to the current target using A* pathfinding.
     * @param batch the SpriteBatch to draw with
     * @param camera the camera for rendering
     * @param playerX the player's x-coordinate
     * @param playerY the player's y-coordinate
     */
    public void render(SpriteBatch batch, Camera camera, float playerX, float playerY) {
        if (!isActive()) return;
        // Load textures lazily
        if (arrows[0] == null) {
            arrows[0] = new Texture("Guide/00_0.png"); // right
            arrows[1] = new Texture("Guide/00_90.png"); // up
            arrows[2] = new Texture("Guide/00_180.png"); // left
            arrows[3] = new Texture("Guide/00_270.png"); // down
        }
        Vector2 target = (stage == 0) ? roncookePos : langwithPos;
        float dist = (float) Math.sqrt((target.x - playerX)*(target.x - playerX) + (target.y - playerY)*(target.y - playerY));
        // Hint is now drawn in UI
        // Compute path
        int pTileX = (int)(playerX / tileW);
        int pTileY = (int)(playerY / tileH);
        int tTileX = (int)(target.x / tileW);
        int tTileY = (int)(target.y / tileH);
        Vector2 nearest = findNearestWalkableTile(pTileX, pTileY, tTileX, tTileY);
        List<Vector2> path = findPath(pTileX, pTileY, (int)nearest.x, (int)nearest.y);
        if (path.size() > 1) {
            // draw path arrows
            int maxArrows = 20;
            for(int i = 0; i < Math.min(path.size() - 1, maxArrows); i++){
                Vector2 current = path.get(i);
                Vector2 next = path.get(i+1);
                float cx = current.x * tileW + tileW / 2f;
                float cy = current.y * tileH + tileH / 2f;
                float nx = next.x * tileW + tileW / 2f;
                float ny = next.y * tileH + tileH / 2f;
                float dx = nx - cx;
                float dy = ny - cy;
                float angleDeg = (float) Math.toDegrees(Math.atan2(dy, dx));
                angleDeg = (angleDeg % 360 + 360) % 360;
                int index = 0;
                float minDiff = Math.abs(angleDeg - 0);
                float[] dirs = {0, 90, 180, 270};
                for (int j = 1; j < 4; j++) {
                    float diff = Math.abs(angleDeg - dirs[j]);
                    if (diff < minDiff) {
                        minDiff = diff;
                        index = j;
                    }
                }
                batch.draw(arrows[index], cx - 10f, cy - 10f, 20f, 20f);
            }
        } else {
            // Fallback: draw arrow next to player pointing to target
            float arrowX = playerX - 130f;
            float arrowY = playerY + 20f;
            float dx = target.x - playerX;
            float dy = target.y - playerY;
            float angleDeg = (float) Math.toDegrees(Math.atan2(dy, dx));
            angleDeg = (angleDeg % 360 + 360) % 360;
            int index = 0;
            float minDiff = Math.abs(angleDeg - 0);
            float[] dirs = {0, 90, 180, 270};
            for (int j = 1; j < 4; j++) {
                float diff = Math.abs(angleDeg - dirs[j]);
                if (diff < minDiff) {
                    minDiff = diff;
                    index = j;
                }
            }
            batch.draw(arrows[index], arrowX + 120f, arrowY - 10f, 20f, 20f);
        }
    }

    private Vector2 findNearestWalkableTile(int startX, int startY, int goalX, int goalY) {
        if (isWalkable(goalX, goalY)) return new Vector2(goalX, goalY);
        for (int dist = 1; dist < 10; dist++) {
            for (int dx = -dist; dx <= dist; dx++) {
                for (int dy = -dist; dy <= dist; dy++) {
                    if (Math.abs(dx) + Math.abs(dy) == dist) {
                        int nx = goalX + dx;
                        int ny = goalY + dy;
                        if (nx >= 0 && ny >= 0 && nx < collisionLayer.getWidth() && ny < collisionLayer.getHeight() && isWalkable(nx, ny)) {
                            return new Vector2(nx, ny);
                        }
                    }
                }
            }
        }
        return new Vector2(goalX, goalY);
    }

    private boolean isWalkable(int x, int y) {
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
        return cell == null || cell.getTile().getId() != mapWallsId;
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
