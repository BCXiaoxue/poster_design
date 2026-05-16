//package com.govy.common.utils;
//
//import com.govy.common.constant.ResultConstant;
//import com.govy.common.properties.CertificateGeneratorProperties;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.net.URL;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Data
//@AllArgsConstructor
//@Slf4j
//public class CertificateGeneratorUtil {
//
//    @SneakyThrows
//    public static MultipartFile create(CertificateGeneratorProperties properties) throws IOException {
//        // 使用类加载器获取资源路径（推荐）
//        URL resourceUrl = CertificateGeneratorUtil.class.getClassLoader().getResource("teamplate/award.png");
//        if (resourceUrl == null) {
//            throw new FileNotFoundException("模板文件未找到: teamplate/award.png");
//        }
//        BufferedImage template = ImageIO.read(resourceUrl);
//
//        Graphics2D g2d = template.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        int width = template.getWidth();
//        int leftMargin = 120;
//        int rightMargin = 120;
//        int sectionSpacing = 90; // 统一大段间距
//        int memberLineHeight = 70;
//        int contentLineHeight = 60;
//
//        // 标题
//        g2d.setFont(new Font("宋体", Font.BOLD, 80));
//        g2d.setColor(Color.BLACK);
//        drawCenteredString(g2d, ResultConstant.POSTER_DESIGN_CONTEST, width, 420);
//
//        // 副标题
//        g2d.setFont(new Font("黑体", Font.BOLD, 70));
//        drawCenteredString(g2d, ResultConstant.HONOR_CERTIFICATE, width, 520);
//
//        // 组名（靠左）
//        g2d.setFont(new Font("宋体", Font.PLAIN, 48));
//        int groupY = 650;
//        drawLeftString(g2d, properties.getUniversity() + properties.getTeamName() +"组：", leftMargin, groupY);
//
//        // 组员（每行3个，自动换行，字体大，居中）
//        g2d.setFont(new Font("楷体", Font.PLAIN, 54));
//        String[] memberArr = properties.getMembers();
//        List<String> memberLines = splitMembersByCount(memberArr, 3, "、");
//        int memberStartY = groupY + sectionSpacing;
//        drawMultilineCenteredString(g2d, memberLines, width, memberStartY, memberLineHeight);
//
//        // 获奖内容（左对齐，首行缩进两格，自动换行）
//        g2d.setFont(new Font("宋体", Font.PLAIN, 42));
//        String awardContent = "荣获" + properties.getCompetitionName() + properties.getAwardLevel();
//        int maxContentWidth = width - leftMargin - rightMargin;
//        // 获奖内容Y = 队员名最后一行Y + sectionSpacing
//        int afterMemberY = memberStartY + memberLines.size() * memberLineHeight + sectionSpacing / 4;
//        List<String> contentLines = splitStringByWidthWithIndent(g2d, awardContent, maxContentWidth, 2);
//        drawMultilineLeftString(g2d, contentLines, leftMargin, afterMemberY, contentLineHeight);
//
//        // 表彰语（靠左，前面空两格，紧跟获奖内容）
//        String praise = "　　特此表彰，以资鼓励。";
//        int afterContentY = afterMemberY + contentLines.size() * contentLineHeight + sectionSpacing;
//        drawLeftString(g2d, praise, leftMargin, afterContentY);
//
//        // 落款（靠右，紧跟表彰语，间距缩小）
//        g2d.setFont(new Font("宋体", Font.PLAIN, 38));
//        int signatureY = afterContentY + sectionSpacing - 30; // 比sectionSpacing略小
//        drawRightString(g2d, ResultConstant.SUTONG_ORGANIZER, width, rightMargin, signatureY);
//
//        // 获取日期
//        LocalDate date = LocalDate.now(); // 当前日期
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
//        String formattedDate = date.format(formatter);
//
//        drawRightString(g2d, formattedDate, width, rightMargin, signatureY + 50);
//
//        g2d.dispose();
//        String FileName = LocalDate.now() + UUID.randomUUID().toString() + ".jpg";
////        ImageIO.write(template, "jpg", new File(FileName));
//        System.out.println("奖状生成成功！");
//
//        // 转换为MultipartFile
//        MultipartFile file = toMultipartFile(template, FileName, "jpg", "image/jpeg");
//        return file;
//    }
//
//    /**
//     * 将 BufferedImage 转换为 MultipartFile
//     *
//     * @param image       图像对象
//     * @param fileName    文件名（带扩展名）
//     * @param formatName  图像格式（如 "jpg", "png"）
//     * @param contentType MIME类型（如 "image/jpeg"）
//     * @return MultipartFile 对象
//     * @throws IOException 如果图像写入失败
//     */
//    public static MultipartFile toMultipartFile(BufferedImage image, String fileName,
//                                                String formatName, String contentType)
//            throws IOException {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, formatName, baos);
//        byte[] imageBytes = baos.toByteArray();
//
//        return new MockMultipartFile(
//                "file",                 // form-data 字段名
//                fileName,               // 原始文件名
//                contentType,            // MIME类型
//                new ByteArrayInputStream(imageBytes)
//        );
//    }
//
//    // 居中
//    private static void drawCenteredString(Graphics2D g2d, String text, int width, int y) {
//        FontMetrics fm = g2d.getFontMetrics();
//        int x = (width - fm.stringWidth(text)) / 2;
//        g2d.drawString(text, x, y);
//    }
//
//    // 多行居中
//    public static void drawMultilineCenteredString(Graphics2D g2d, List<String> lines, int width, int startY, int lineHeight) {
//        for (int i = 0; i < lines.size(); i++) {
//            drawCenteredString(g2d, lines.get(i), width, startY + i * lineHeight);
//        }
//    }
//
//    // 靠左
//    private static void drawLeftString(Graphics2D g2d, String text, int leftMargin, int y) {
//        g2d.drawString(text, leftMargin, y);
//    }
//
//    // 多行左对齐
//    public static void drawMultilineLeftString(Graphics2D g2d, List<String> lines, int leftMargin, int startY, int lineHeight) {
//        for (int i = 0; i < lines.size(); i++) {
//            drawLeftString(g2d, lines.get(i), leftMargin, startY + i * lineHeight);
//        }
//    }
//
//    // 靠右
//    private static void drawRightString(Graphics2D g2d, String text, int width, int rightMargin, int y) {
//        FontMetrics fm = g2d.getFontMetrics();
//        int x = width - rightMargin - fm.stringWidth(text);
//        g2d.drawString(text, x, y);
//    }
//
//    // 自动分行，首行缩进n个全角空格
//    public static List<String> splitStringByWidthWithIndent(Graphics2D g2d, String text, int maxWidth, int indentCount) {
//        List<String> lines = new ArrayList<>();
//        String indent = "　".repeat(indentCount); // 全角空格
//        StringBuilder line = new StringBuilder(indent);
//        int i = 0;
//        while (i < text.length()) {
//            line.append(text.charAt(i));
//            if (g2d.getFontMetrics().stringWidth(line.toString()) > maxWidth) {
//                line.deleteCharAt(line.length() - 1);
//                lines.add(line.toString());
//                line = new StringBuilder(); // 后续行无缩进
//            } else {
//                i++;
//            }
//        }
//        if (line.length() > 0) {
//            lines.add(line.toString());
//        }
//        // 后续行去掉缩进
//        for (int j = 1; j < lines.size(); j++) {
//            if (lines.get(j).startsWith(indent)) {
//                lines.set(j, lines.get(j).substring(indent.length()));
//            }
//        }
//        return lines;
//    }
//
//    // 队员名字每行n个，自动换行
//    public static List<String> splitMembersByCount(String[] members, int countPerLine, String delimiter) {
//        List<String> lines = new ArrayList<>();
//        StringBuilder line = new StringBuilder();
//        for (int i = 0; i < members.length; i++) {
//            if (line.length() > 0) {
//                line.append(delimiter);
//            }
//            line.append(members[i]);
//            if ((i + 1) % countPerLine == 0 || i == members.length - 1) {
//                lines.add(line.toString());
//                line = new StringBuilder();
//            }
//        }
//        return lines;
//    }
//}





package com.govy.common.utils;

import com.govy.common.constant.ResultConstant;
import com.govy.common.properties.CertificateGeneratorProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
public class CertificateGeneratorUtil {

    @SneakyThrows
    public static MultipartFile create(CertificateGeneratorProperties properties) throws IOException {
        // 使用类加载器获取资源路径（推荐）
        URL resourceUrl = CertificateGeneratorUtil.class.getClassLoader().getResource("teamplate/award.png");
        if (resourceUrl == null) {
            throw new FileNotFoundException("模板文件未找到: teamplate/award.png");
        }
        BufferedImage template = ImageIO.read(resourceUrl);

        Graphics2D g2d = template.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = template.getWidth();
        int leftMargin = 120;
        int rightMargin = 120;
        int sectionSpacing = 90; // 统一大段间距
        int memberLineHeight = 70;
        int contentLineHeight = 60;

        // 标题
        g2d.setFont(new Font("宋体", Font.BOLD, 80));
        g2d.setColor(Color.BLACK);
        drawCenteredString(g2d, ResultConstant.POSTER_DESIGN_CONTEST, width, 420);

        // 副标题
        g2d.setFont(new Font("黑体", Font.BOLD, 70));
        drawCenteredString(g2d, ResultConstant.HONOR_CERTIFICATE, width, 520);

        // 组名（靠左）
        g2d.setFont(new Font("宋体", Font.PLAIN, 48));
        int groupY = 650;
        drawLeftString(g2d, properties.getUniversity() + properties.getTeamName() + "组：", leftMargin, groupY);

        // 组员（每行3个，自动换行，字体大，居中）
        g2d.setFont(new Font("楷体", Font.PLAIN, 54));
        String[] memberArr = properties.getMembers();
        List<String> memberLines = splitMembersByCount(memberArr, 3, "、");
        int memberStartY = groupY + sectionSpacing;
        drawMultilineCenteredString(g2d, memberLines, width, memberStartY, memberLineHeight);

        // 获奖内容（左对齐，首行缩进两格，自动换行）
        g2d.setFont(new Font("宋体", Font.PLAIN, 42));
        String awardContentPrefix = "荣获" + properties.getCompetitionName();
        String awardLevel = properties.getAwardLevel();
        int maxContentWidth = width - leftMargin - rightMargin;
        int afterMemberY = memberStartY + memberLines.size() * memberLineHeight + sectionSpacing / 4;

        // 先绘制前缀（非加粗）
        List<String> prefixLines = splitStringByWidthWithIndent(g2d, awardContentPrefix, maxContentWidth, 2);
        drawMultilineLeftString(g2d, prefixLines, leftMargin, afterMemberY, contentLineHeight);

        // 计算前缀最后一行文本的结束位置
        FontMetrics fm = g2d.getFontMetrics();
        int lastPrefixLineY = afterMemberY + (prefixLines.size() - 1) * contentLineHeight;
        String lastPrefixLine = prefixLines.get(prefixLines.size() - 1);
        int lastPrefixLineWidth = fm.stringWidth(lastPrefixLine);

        // 设置加粗字体并绘制奖项级别
        g2d.setFont(new Font("宋体", Font.BOLD, 42));
        drawLeftString(g2d, awardLevel, leftMargin + lastPrefixLineWidth, lastPrefixLineY);

        // 表彰语（靠左，前面空两格，紧跟获奖内容）
        String praise = "　　特此表彰，以资鼓励。";
        int afterContentY = lastPrefixLineY + contentLineHeight + sectionSpacing;
        drawLeftString(g2d, praise, leftMargin, afterContentY);

        // 落款（靠右，紧跟表彰语，间距缩小）
        g2d.setFont(new Font("宋体", Font.PLAIN, 38));
        int signatureY = afterContentY + sectionSpacing - 30; // 比sectionSpacing略小
        drawRightString(g2d, ResultConstant.SUTONG_ORGANIZER, width, rightMargin, signatureY);

        // 获取日期
        LocalDate date = LocalDate.now(); // 当前日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
        String formattedDate = date.format(formatter);

        drawRightString(g2d, formattedDate, width, rightMargin, signatureY + 50);

        g2d.dispose();
        String FileName = LocalDate.now() + UUID.randomUUID().toString() + ".jpg";
        System.out.println("奖状生成成功！");

        // 转换为MultipartFile
        MultipartFile file = toMultipartFile(template, FileName, "jpg", "image/jpeg");
        return file;
    }

    /**
     * 将 BufferedImage 转换为 MultipartFile
     *
     * @param image       图像对象
     * @param fileName    文件名（带扩展名）
     * @param formatName  图像格式（如 "jpg", "png"）
     * @param contentType MIME类型（如 "image/jpeg"）
     * @return MultipartFile 对象
     * @throws IOException 如果图像写入失败
     */
    public static MultipartFile toMultipartFile(BufferedImage image, String fileName,
                                                String formatName, String contentType)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, baos);
        byte[] imageBytes = baos.toByteArray();

        return new MockMultipartFile(
                "file",                 // form-data 字段名
                fileName,               // 原始文件名
                contentType,            // MIME类型
                new ByteArrayInputStream(imageBytes)
        );
    }

    // 居中
    private static void drawCenteredString(Graphics2D g2d, String text, int width, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }

    // 多行居中
    public static void drawMultilineCenteredString(Graphics2D g2d, List<String> lines, int width, int startY, int lineHeight) {
        for (int i = 0; i < lines.size(); i++) {
            drawCenteredString(g2d, lines.get(i), width, startY + i * lineHeight);
        }
    }

    // 靠左
    private static void drawLeftString(Graphics2D g2d, String text, int leftMargin, int y) {
        g2d.drawString(text, leftMargin, y);
    }

    // 多行左对齐
    public static void drawMultilineLeftString(Graphics2D g2d, List<String> lines, int leftMargin, int startY, int lineHeight) {
        for (int i = 0; i < lines.size(); i++) {
            drawLeftString(g2d, lines.get(i), leftMargin, startY + i * lineHeight);
        }
    }

    // 靠右
    private static void drawRightString(Graphics2D g2d, String text, int width, int rightMargin, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = width - rightMargin - fm.stringWidth(text);
        g2d.drawString(text, x, y);
    }

    // 自动分行，首行缩进n个全角空格
    public static List<String> splitStringByWidthWithIndent(Graphics2D g2d, String text, int maxWidth, int indentCount) {
        List<String> lines = new ArrayList<>();
        String indent = "　".repeat(indentCount); // 全角空格
        StringBuilder line = new StringBuilder(indent);
        int i = 0;
        while (i < text.length()) {
            line.append(text.charAt(i));
            if (g2d.getFontMetrics().stringWidth(line.toString()) > maxWidth) {
                line.deleteCharAt(line.length() - 1);
                lines.add(line.toString());
                line = new StringBuilder(); // 后续行无缩进
            } else {
                i++;
            }
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        // 后续行去掉缩进
        for (int j = 1; j < lines.size(); j++) {
            if (lines.get(j).startsWith(indent)) {
                lines.set(j, lines.get(j).substring(indent.length()));
            }
        }
        return lines;
    }

    // 队员名字每行n个，自动换行
    public static List<String> splitMembersByCount(String[] members, int countPerLine, String delimiter) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < members.length; i++) {
            if (line.length() > 0) {
                line.append(delimiter);
            }
            line.append(members[i]);
            if ((i + 1) % countPerLine == 0 || i == members.length - 1) {
                lines.add(line.toString());
                line = new StringBuilder();
            }
        }
        return lines;
    }
}