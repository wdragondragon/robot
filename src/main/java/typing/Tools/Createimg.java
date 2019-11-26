package typing.Tools;


import typing.ShortCoding.BetterTyping;
import typing.ShortCoding.SubscriptInstance;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static java.io.File.separator;

public class Createimg {

    public static String graphicsGeneration(List<List<List<String>>> allValue, List<String> titles,
                                            List<String[]> headers , String receiver, int totalcol,
                                            HashMap<Integer,List<Double>> rankmap) throws Exception {
        int rows = 0;
        int maxfont = 0;
        Map<String,String> QQdo = new HashMap();
        for (List<List<String>> typeV : allValue) {
            if (typeV != null && typeV.size() > 0) {
                rows += (2+typeV.size());
            }
            for (List<String> strings : typeV) {
                maxfont = strings.get(0).getBytes().length>maxfont?strings.get(0).getBytes().length:maxfont;
                if(strings.get(0).indexOf("%")!=-1) {
                    String s[] = strings.get(0).split("%");
                    QQdo.put(s[0], s[1]);
                    strings.set(0,s[0]);
                }
            }
        }
        // 实际数据行数+标题+备注
        int numwidth = 50;
        int totalrow = 1+rows;
        int namewidth = maxfont * 7;
        int otherwidth = 87;

        int imageWidth = numwidth + namewidth  + otherwidth*(totalcol-2) + 20;
        int imageHeight = totalrow * 30 + 20;
        int rowheight = 30;
        int startHeight = 10;
        int startWidth = 10;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        // 画横线
        int startH = 2;

        graphics.setColor(new Color(200,200,200));
        for (int j = 0; j < totalrow - 1; j++) {
            graphics.drawLine(startWidth, startHeight + (j + startH) * rowheight, imageWidth - 10,
                    startHeight + (j + startH) * rowheight);
        }
        // 画竖

        int rightLine = 0 ;
        for (List<List<String>> typeV : allValue) {

            if (typeV != null && typeV.size() > 0) {
                for (int k = 0; k < totalcol+1; k++) {
                    rightLine = getRightMargin(k,startWidth, namewidth,otherwidth,imageWidth,totalcol);
                    graphics.drawLine(rightLine, startHeight + startH*rowheight, rightLine,
                            startHeight + (typeV.size()+1+startH)*rowheight);
                }
                startH+=2+typeV.size();
            }
        }
        //画背景
        graphics.setColor(new Color(190,25,0));
        startH = 2;
        int redstartH = 3;
        for (List<List<String>> typeV : allValue) {
            if (typeV != null && typeV.size() > 0) {
                graphics.fillRect(startWidth + 1, startHeight + startH * rowheight + 1, imageWidth - startWidth - 10, rowheight );
                startH += 2 + typeV.size();
            }
            graphics.setColor(new Color(150,0,0));
            for (int temp = 0; temp < typeV.size(); temp++) {
                List strings = typeV.get(temp);
                if (strings != null) {
                    graphics.fillRect(startWidth + 1,startHeight + redstartH*rowheight +1 , imageWidth - startWidth - 10,rowheight );
                }
                redstartH++;
                if(temp==2)break;
            }

        }
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(imageWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setStroke(s);

        // 设置字体
        Font font = new Font("宋体", Font.BOLD, 20);
        graphics.setFont(font);
        graphics.setColor(Color.black);
        // 写标题
        startH = 1;
        int i = 0;
        for (List<List<String>> typeV : allValue) {
            if (typeV != null && typeV.size() > 0) {
                int strWidth = graphics.getFontMetrics().stringWidth(titles.get(i));
                graphics.drawString(titles.get(i), (imageWidth-strWidth)/2, startHeight + startH*rowheight - 10);
                startH+=2+typeV.size();
            }
            i++;
        }
        font = new Font("微软雅黑", Font.BOLD, 17);
        Font font1 = new Font("微软雅黑",Font.PLAIN,10);
        //写时间
        graphics.setFont(font);
        graphics.setColor(Color.gray);
        startH = 2;
        String time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int strWidth = graphics.getFontMetrics().stringWidth(time);
        graphics.drawString(time,imageWidth-20-strWidth,startHeight + startH*rowheight -10);
        // 写入表头
        font = new Font("微软雅黑", Font.PLAIN, 17);
        graphics.setColor(Color.WHITE);
        graphics.setFont(font);
        startH = 3;
        i = 0;
        for (List<List<String>> typeV : allValue) {
            if (typeV != null && typeV.size() > 0) {
                String[] headCells = headers.get(i);
                for (int m = 0; m < headCells.length; m++) {
                    strWidth = graphics.getFontMetrics().stringWidth(headCells[m].toString());
                    rightLine = getRightMargin(m,startWidth, namewidth,otherwidth,imageWidth,totalcol);
                    if(m==0)
                        rightLine = rightLine + (numwidth-strWidth)/2;
                    else if(m==1)
                        rightLine = rightLine + (namewidth-strWidth)/2;
                    else
                        rightLine = rightLine + (otherwidth-strWidth)/2;
                    graphics.drawString(headCells[m].toString(), rightLine,
                            startHeight + rowheight * startH - 10);
                }
                startH+=2+typeV.size();
            }
            i++;
        }

        // 写入内容
        graphics.setColor(Color.white);
        startH = 4;
        i = 0;
        for (List<List<String>> typeV : allValue) {
            if (typeV != null && typeV.size() > 0) {
                for (int n = 0; n < typeV.size(); n++) {
                    List<String> arr = typeV.get(n);
                    for (int l = 0; l < arr.size()+1; l++) {
                        rightLine = getRightMargin(l,startWidth, namewidth,otherwidth,imageWidth,totalcol)+5;
                        //画正文
                        graphics.setFont(font);
                        if (n<3)graphics.setColor(Color.white);
                        if (n >= 3) graphics.setColor(Color.black);
                        if(l==0){
                            strWidth = graphics.getFontMetrics().stringWidth(String.valueOf(n+1));
                            graphics.drawString(String.valueOf(n+1), rightLine+(numwidth-strWidth)/2-5,
                                    startHeight + rowheight * (n + startH) - 8);
                        }else {
                             strWidth = graphics.getFontMetrics().stringWidth(arr.get(l-1));
                            if(l==1) {
                                graphics.drawString(arr.get(l - 1), rightLine,
                                        startHeight + rowheight * (n + startH) - 8);
                                //画捐赠者
                                if(QQdo.containsKey(arr.get(l - 1))) {
                                    String sign = QQdo.get(arr.get(l - 1));
//                                    graphics.setColor(Color.green);
                                    graphics.setColor(new Color(150,0,0));
                                    graphics.fillRect(rightLine + strWidth + 3, rowheight * (n + startH) - 5, 10, 10);
                                    graphics.setColor(Color.white);
                                    graphics.setFont(new Font("微软雅黑", Font.BOLD, 10));
                                    graphics.drawString(sign, rightLine + strWidth + 3,
                                            rowheight * (n + startH) + 4);
                                }
                            }
                            else
                                graphics.drawString(arr.get(l - 1).toString(), rightLine+(otherwidth-strWidth)/2-5,
                                        startHeight + rowheight * (n + startH) - 8);
                        }
                        //画下标
                        if (n<3)graphics.setColor(Color.white);
                        if (n >= 3) graphics.setColor(Color.black);
                        graphics.setFont(font1);
                        if(rankmap.containsKey(l)&&!arr.get(l-1).equals("无")){
                            List<Double> ranklist = rankmap.get(l);
                            int rank = ranklist.indexOf(Double.parseDouble(arr.get(l-1)))+1;
                            if(rank!=0)
                                graphics.drawString(String.valueOf(rank),rightLine+(otherwidth+strWidth)/2-2,
                                        startHeight + rowheight * (n + startH) - 8);
                        }
                    }
                }
                startH+=2+typeV.size();
            }
            i++;
        }
        String path = "typinggroup"+separator+"zmc.png";
        graphics.drawImage(image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(image, "png",
                new File("/root/coolq/data/image/"
//                new File("C:\\Users\\Lenovo\\Desktop\\酷Q Pro\\data\\image\\"
                        +path));
        return path;
    }

    /**
     * 获取竖线和文字的水平位置
     * @param k
     * @param startWidth
     * @param
     * @param imageWidth
     * @return
     */
    private static int getRightMargin(int k, int startWidth, int namewidth,int otherwidth, int imageWidth,int totalcol) {
        int rightLine = 0;
        if (k == 0) {
            rightLine = startWidth;
        } else if (k == 1) {
            rightLine = startWidth + 50;
        } else if (k == 2) {
            rightLine = startWidth + 50 + namewidth;
        } else if (k >= 3 &&k<totalcol) {
            rightLine = startWidth +50 + namewidth + (k - 2) * otherwidth;
        } else if (k == totalcol)
            rightLine = imageWidth - 10;
        return rightLine;
    }


    public static void initChartData(){
        List<List<List<String>>> allValue = new ArrayList<>();
//        List<String> content1 = Arrays.asList(new String[]{"刘丹丹","25","163cm","未婚"});
//        List<String> content2 = Arrays.asList(new String[]{"刘丹丹","25","163cm","未婚"});
//        List<String> content3 = Arrays.asList(new String[]{"刘丹丹","宿迁","本科","未婚"});
        List<List<String>> contentArray1 = new ArrayList<>();

        for(int i = 0;i<10;i++){
            contentArray1.add(Arrays.asList(new String[]{"无聊就是个弟弟","300","5.00","1.00","1234","1234","1234","100"}));
        }
//        contentArray1.add(content1);
//        contentArray1.add(content2);
//        List<List<String>> contentArray2 = new ArrayList<>();
//        contentArray2.add(content3);
        allValue.add(contentArray1);
//        allValue.add(contentArray2);

        List<String[]> headTitles = new ArrayList<>();
        String[] h1 = new String[]{"序号","名字","成绩","击键","码长","退格","回改","选重","错字"};
//        String[] h2 = new String[]{"名字","籍贯","学历","婚姻"};
        headTitles.add(h1);
//        headTitles.add(h2);

        List<String> titles = new ArrayList<>();
        titles.add("跟打成绩");
//        titles.add("SQE部门人员统计");
        try {
//            graphicsGeneration(allValue,titles,headTitles ,"",9);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    enum typeColor{
        dan(0,Color.black,new Font("微软雅黑",  Font.BOLD,20)),
        q(1,new Color(128, 138, 135),new Font("微软雅黑",  Font.BOLD,20)),
        cq(2,new Color(128, 138, 135),new Font("微软雅黑",  Font.BOLD+ Font.ITALIC ,20)),
        sj(3,Color.BLUE,new Font("微软雅黑",  Font.BOLD,20)),
        csj(4,Color.BLUE,new Font("微软雅黑",  Font.BOLD+ Font.ITALIC,20)),
        ej(5,Color.ORANGE,new Font("微软雅黑",  Font.BOLD,20)),
        cej(6,Color.ORANGE,new Font("微软雅黑",  Font.BOLD+ Font.ITALIC ,20));
        private Color color;
        private int type;
        private Font font;
        typeColor(int type,Color color,Font font){
            this.type = type;
            this.color = color;
            this.font = font;
        }
        public static Color getColor(int type){
            for (typeColor typeColor : typeColor.values()) {
                if(typeColor.type == type)
                    return typeColor.color;
            }
            return null;
        }
        public static  Font getFont(int type){
            for (typeColor typeColor : typeColor.values()) {
                if(typeColor.type == type)
                    return typeColor.font;
            }
            return null;
        }
    }
    public static String drawTipImg(SubscriptInstance[] subscriptInstances) throws IOException {
            Font font = new Font("宋体", Font.BOLD, 20);
        class DrawSub{
            int x,y;
            String words,wordsCode;
            Color color;
            Font font;
            DrawSub(int x,int y,String words,String wordsCode,Color color,Font font){
                this.x = x;
                this.y = y;
                this.words = words;
                this.wordsCode = wordsCode;
                this.color = color;
                this.font = font;
            }
        }

        List<DrawSub> drawSubList = new ArrayList<>();
        int imageWidth = 1000;
        int imageHeight = 65;

        int imageWidthTemp = 10;
        for(int i = 0;i<subscriptInstances.length;i++){
            String words,wordsCode;
            int type = subscriptInstances[i].getType();
            if(subscriptInstances[i].isUseWordSign()) {
                i = subscriptInstances[i].getNext();
                words = subscriptInstances[i].getShortCodePreInfo().getWords();
                wordsCode = subscriptInstances[i].getShortCodePreInfo().getWordsCode();
            }else{
                words = subscriptInstances[i].getWord();
                wordsCode = subscriptInstances[i].getWordCode();
            }
            int wordsLength = words.length();
            int wordsCodeLength = wordsCode.length();
            int length = wordsLength>wordsCodeLength?wordsLength:wordsCodeLength;
            if(imageWidthTemp +length*20  > imageWidth) {
                imageWidthTemp = 10;
                imageHeight += 65;
            }
            DrawSub drawSub = new DrawSub(imageWidthTemp,imageHeight-40,words,wordsCode,typeColor.getColor(type),typeColor.getFont(type));
            drawSubList.add(drawSub);
            imageWidthTemp += length*20 + 10;

        }

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(new Color(238, 238, 238));
        graphics.setFont(font);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        int x = 0,y = 0;
        for(DrawSub drawSub:drawSubList){
            String words = drawSub.words;
            String wordsCode = drawSub.wordsCode;
            graphics.setColor(drawSub.color);
            graphics.setFont(drawSub.font);
            graphics.drawString(words, drawSub.x, drawSub.y);
            graphics.drawString(wordsCode, drawSub.x, drawSub.y+30);
        }
//        graphics.drawString(titles.get(i), (imageWidth-strWidth)/2, startHeight + startH*rowheight - 10);
        graphics.setColor(Color.black);
        for(int i = 0;i<imageHeight/65;i++){
            graphics.drawLine(0, (i+1)*65,imageWidth,(i+1)*65);
        }
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(imageWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setStroke(s);
        String path = "typinggroup"+separator+"zmc.png";
        graphics.drawImage(image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(image, "png",
                new File("/root/coolq/data/image/"
//                new File("C:\\Users\\Lenovo\\Desktop\\酷Q Pro\\data\\image\\"
                        +path));
        return path;
    }
    public static void main(String[] args) throws IOException {
        BetterTyping betterTyping = new BetterTyping("词组提示码表");
        betterTyping.changecolortip("最新的测试版是哪个最新的测试版是哪个最新的测试版是哪个最新的测试版是哪个最新的测试版是哪个最新的测试版是哪个");
        betterTyping.compalllength();
        SubscriptInstance[] subscriptInstances = betterTyping.getSubscriptInstances();
        System.out.println(betterTyping.getDingShowStr());
        drawTipImg(subscriptInstances);
    }
}




