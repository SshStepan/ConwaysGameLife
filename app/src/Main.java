import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HexFormat;

public class Main {

    public static int countNeigbours(boolean[][] board, int x, int y) {
        int h = board.length;
        int w = board[y].length;
        int count = 0;

        for (int yLocal = y - 1; yLocal <= y + 1; yLocal++) {
            for (int xLocal = x - 1; xLocal <= x + 1; xLocal++) {
                if (board[Math.floorMod(yLocal, h)][Math.floorMod(xLocal, w)]) count++;
            }
        }

        if (board[y][x]) count--;

        return count;
    }

    public static boolean[][] setBoardWithImage(String imagePath) {
        boolean[][] board = null;
        try  {
            BufferedImage image = ImageIO.read(new File(imagePath));
            final int HEIGHT = image.getHeight();
            final int WIDTH = image.getWidth();
            board = new boolean[HEIGHT][WIDTH];

            BufferedImage firstSnap = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    if (image.getRGB(x,y) != -1) {
                        board[y][x] = true;
                        firstSnap.setRGB(x,y,1);
                    } else {
                        firstSnap.setRGB(x,y, HexFormat.fromHexDigits("FFFFFF"));
                    }
                }
            }
            String format = imagePath.substring(imagePath.lastIndexOf('.') + 1);
            ImageIO.write(firstSnap, format, new File(String.format("./snaps/snap%d.%s", 0, format)));

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Wrong file format!");
        }
        return board;
    }

    public static void conwayLife(boolean[][] board, int snaps, String format) {
        int h = board.length;
        int w = board[0].length;

        for (int snap = 1; snap < snaps; snap++) {
            BufferedImage tempOut = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int n = countNeigbours(board,x,y);
                    if (board[y][x] && (n == 3 || n == 2)) {
                        tempOut.setRGB(x,y,1);
                        board[y][x] = true;
                    } else if (n == 3) {
                        tempOut.setRGB(x,y,1);
                        board[y][x] = true;
                    } else {
                        tempOut.setRGB(x,y,HexFormat.fromHexDigits("FFFFFF"));
                        board[y][x] = false;
                    }
                }
            }
            try {
                ImageIO.write(tempOut, format, new File(String.format("./snaps/snap%d.%s", snap, format)));
            } catch (IOException e) {
                System.out.println("Wrong image format!");
            }
        }
    }
    public static void main(String[] args) {
        String imagePath = "./img/image.jpg";
        boolean[][] board = setBoardWithImage(imagePath);
        final int AMOUNT_OF_SNAPS = 1000;
        String format = imagePath.substring(imagePath.lastIndexOf('.') + 1);
        conwayLife(board, AMOUNT_OF_SNAPS, format);
    }
}