import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        FileReader code = new FileReader("input.txt");
        BufferedReader in = new BufferedReader(code);
        String s = "";
        String g = "";
        while((s=in.readLine())!=null)
            g+=s;
        byte [] arr = g.getBytes(Charset.forName("UTF-8"));
        ImageProcess impro = new ImageProcess();
        BufferedImage img = impro.fetchImage();
        impro.hideText(img,arr);
    }
}

class ImageProcess{
    BufferedImage fetchImage() throws Exception{
        File f = new File("cateika.jpg");
        BufferedImage img = ImageIO.read(f);
        return img;
    }

    void hideText(BufferedImage img , byte [] txt) throws Exception{
        int i = 0;
        int j = 0;
        for(byte b : txt){
            for(int k=7;k>=0;k--){
                Color c = new Color(img.getRGB(j,i));
                byte blue = (byte)c.getBlue();
                int red = c.getRed();
                int green = c.getGreen();
                int bitVal = (b >>> k) & 1;
                blue = (byte)((blue & 0xFE)| bitVal);
                Color newColor = new Color(red,green,(blue & 0xFF));
                img.setRGB(j,i,newColor.getRGB());
                j++;
            }
            i++;
        }

        System.out.println("Text Hidden");
        createImgWithMsg(img);
        System.out.println("Decode? Y or N");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        switch(in.readLine().trim()){
            case "Y":
            case "y":
            {
                String k = revealMsg(txt.length,0);
                System.out.println("Text was: " + k);
            }
            break;
            default:
                System.out.println("End");
                break;
        }
    }

    void createImgWithMsg(BufferedImage img){
        try{
            File ouptut = new File("kat.png");
            ImageIO.write(img, "png", ouptut);
        }
        catch(Exception ex)
        {}
    }

    BufferedImage newImageFetch(){
        File f = new File("kat.png");
        BufferedImage img = null;
        try{
            img = ImageIO.read(f);
        }
        catch(Exception ex)
        {}
        return img;
    }

    String revealMsg(int msgLen , int offset){
        BufferedImage img = newImageFetch();
        byte [] msgBytes = extractHiddenBytes(img,msgLen,offset);
        if(msgBytes == null)
            return null;
        String msg = new String(msgBytes);
        return (msg);
    }

    byte[] extractHiddenBytes(BufferedImage img , int size , int offset){
        int i = 0;
        int j = 0;
        byte [] hiddenBytes = new byte[size];
        for(int l=0;l<size;l++){
            for(int k=0 ; k<8 ; k++){
                Color c = new Color(img.getRGB(j,i));
                byte blue = (byte)c.getBlue();
                hiddenBytes[l] = (byte) ((hiddenBytes[l]<<1)|(blue&1));
                j++;
            }
            i++;
        }
        return hiddenBytes;
    }
}