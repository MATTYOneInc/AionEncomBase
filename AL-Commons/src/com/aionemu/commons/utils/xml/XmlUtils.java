package com.aionemu.commons.utils.xml;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public abstract class XmlUtils {
   private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
   private static final TransformerFactory tf = TransformerFactory.newInstance();

   public static Document getDocument(String xmlSource) {
      Class var1 = XmlUtils.class;
      synchronized(XmlUtils.class) {
         Document document = null;
         if (xmlSource != null) {
            try {
               Reader stream = new StringReader(xmlSource);
               DocumentBuilder db = dbf.newDocumentBuilder();
               document = db.parse(new InputSource(stream));
            } catch (Exception var6) {
               throw new RuntimeException("Error converting string to document", var6);
            }
         }

         return document;
      }
   }

   public static String getString(Document document) {
      Class var1 = XmlUtils.class;
      synchronized(XmlUtils.class) {
         String var10000;
         try {
            DOMSource domSource = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            var10000 = writer.toString();
         } catch (TransformerException var7) {
            throw new RuntimeException(var7);
         }

         return var10000;
      }
   }

   public static Schema getSchema(String schemaString) {
      Schema schema = null;

      try {
         if (schemaString != null) {
            SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            StreamSource ss = new StreamSource();
            ss.setReader(new StringReader(schemaString));
            schema = sf.newSchema(ss);
         }

         return schema;
      } catch (Exception var4) {
         throw new RuntimeException("Failed to create schemma from string: " + schemaString, var4);
      }
   }

   public static Schema getSchema(URL schemaURL) {
      Schema schema = null;

      try {
         if (schemaURL != null) {
            SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            schema = sf.newSchema(schemaURL);
         }

         return schema;
      } catch (Exception var3) {
         throw new RuntimeException("Failed to create shcemma from URL " + schemaURL, var3);
      }
   }

   public static void validate(Schema schema, Document document) {
      Validator validator = schema.newValidator();

      try {
         validator.validate(new DOMSource(document));
      } catch (Exception var4) {
         throw new RuntimeException("Failed to validate document", var4);
      }
   }

   static {
      dbf.setNamespaceAware(true);
   }
}
