package com.jecelyin.editor.v2.tools;

import static com.jecelyin.editor.v2.tools.Tool.fileNameToResName;
import static com.jecelyin.editor.v2.tools.Tool.o;
import static com.jecelyin.editor.v2.tools.Tool.readFile;
import static com.jecelyin.editor.v2.tools.Tool.space;
import static com.jecelyin.editor.v2.tools.Tool.textString;
import static com.jecelyin.editor.v2.tools.Tool.writeFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XML2Bin {

  private static File assetsPath;
  private static File highlightPath;
  private static File rawPath;
  private static MessagePacker packer;

  public static void main(String[] args) {
    File f = new File(".");
    String path = f.getAbsolutePath();

    highlightPath = new File(path, "app/src/main/java/com/jecelyin/editor/v2/highlight");
    assetsPath = new File(path, "tools/assets");
    File syntax = new File(assetsPath, "syntax");

    rawPath = new File(path, "app/src/main/res/raw");
    for (File f2 : rawPath.listFiles()) {
      f2.delete();
    }

    File[] files = syntax.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File file, String s) {
        return s.endsWith(".xml");
      }
    });

    StringBuilder mapCode = new StringBuilder();
    try {
      for (File file : files) {
        o("File: %s", file.getName());
        parseXml(file, mapCode);
      }

      String langMap = readFile(new File(assetsPath, "lang_map.tpl"));
      langMap = langMap.replace("@CASE_LIST@", mapCode.toString());
      writeFile(new File(highlightPath, "LangMap.java"), langMap);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void parseXml(final File file, StringBuilder mapCode) throws Exception {
    String clsName = fileNameToResName(file.getName()) + "_lang";

    DocumentBuilderFactory dbFactory
        = DocumentBuilderFactory.newInstance();
//        dbFactory.setValidating(false);
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    dBuilder.setEntityResolver(new EntityResolver() {
      @Override
      public InputSource resolveEntity(String s, String systemId) throws SAXException, IOException {
        if (systemId.contains("xmode.dtd")) {
          return new InputSource(new FileInputStream(new File(assetsPath, "xmode.dtd")));
        }
        return null;
      }
    });
    Document doc = dBuilder.parse(file);
    doc.getDocumentElement().normalize();

    NodeList nList = doc.getChildNodes();
    int length = nList.getLength();
    for (int i = 0; i < length; i++) {
      Node item = nList.item(i);
      o("node " + item.getNodeName() + " " + item.getNodeType());
      if (item.getNodeType() == Node.ELEMENT_NODE) {

        if (!item.getNodeName().equals("MODE")) {
          throw new RuntimeException("!MODE: " + item.getNodeName());
        }
        File langFile = new File(rawPath, clsName);
        packer = MessagePack.newDefaultPacker(new FileOutputStream(langFile));

        handleChild((Element) item);

        mapCode.append(space(12)).append("case ").append(textString(file.getName()))
            .append(": return R.raw.").append(clsName).append(";\n");

        packer.close();
      }
    }
  }


  private static void handleMode(Element element) throws IOException {
    NodeList childNodes = element.getChildNodes();
    int length = childNodes.getLength();

    ArrayList<Node> items = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      Node item = childNodes.item(i);
      if (item.getNodeType() != Node.ELEMENT_NODE) {
        continue;
      }

      items.add(item);
    }

    for (Node item : items) {
      handleChild(item);
    }

  }

  private static void handleChild(Node node) throws IOException {
    String tag = node.getNodeName();

    packer.packString(tag);

    StringBuilder text = new StringBuilder();
    List<Node> nodes = nodes(node, text);
    packer.packString(text.toString());

    HashMap<String, String> attrs = attrs(node);
    packer.packMapHeader(attrs.size());
    for (Map.Entry<String, String> entry : attrs.entrySet()) {
      packer.packString(entry.getKey());
      packer.packString(entry.getValue());
    }

    packer.packInt(nodes.size());
    for (Node child : nodes) {
      handleChild(child);
    }

  }

  private static List<Node> nodes(Node node, StringBuilder text) {
    NodeList childNodes = node.getChildNodes();
    int length = childNodes.getLength();

    List<Node> list = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
      Node item = childNodes.item(i);
      if (item.getNodeType() == Node.TEXT_NODE) {
        String str = item.getTextContent().toString().trim();
        if (str.isEmpty()) {
          continue;
        }
        text.append(str);
        continue;
      } else if (item.getNodeType() != Node.ELEMENT_NODE) {
        continue;
      }
      list.add(item);
    }

    return list;
  }

  private static HashMap<String, String> attrs(Node node) {
    HashMap<String, String> map = new HashMap<String, String>();

    NamedNodeMap childNodes = node.getAttributes();
    if (childNodes == null) {
      return map;
    }

    int length = childNodes.getLength();

    for (int i = 0; i < length; i++) {
      Node item = childNodes.item(i);
      map.put(item.getNodeName(), item.getNodeValue());
    }

    return map;
  }


}
