/*     */ 
/*     */ import com.dstm.common.exception.CommonConfigException;
/*     */ import com.dstm.common.log.DSTMLogger;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.exolab.castor.xml.Unmarshaller;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public class XMLConfigLoaderAA
/*     */ {
/*  47 */   public static final DSTMLogger LOG = DSTMLogger.getLogger(XMLConfigLoaderAA.class);
/*     */   public URL fileURL;
/*     */   public static XMLConfigLoaderAA instance;
/*     */ 
/*     */   public URL getFileURL()
/*     */   {
/*  75 */     return this.fileURL;
/*     */   }
/*     */ 
/*     */   public Element load(String conf)
/*     */   {
				System.out.println("load in conf "+conf);
/*  87 */     return loadDocument(conf).getDocumentElement();
/*     */   }
/*     */ 
/*     */   public static XMLConfigLoaderAA getInstance()
/*     */   {
				
/*  97 */     return new XMLConfigLoaderAA();
/*     */   }
/*     */ 
/*     */   public Object loadToCastor(String conf, Class cclass)
/*     */   {
				System.out.println("loadToCastor value is :"+conf+ "  class is :"+cclass);
/*     */     try
/*     */     { 
/* 112 */       Element _el = load(conf);
				System.out.println("loadToCastor value is :"+_el);
/* 113 */       return Unmarshaller.unmarshal(cclass, _el);
/*     */     }
/*     */     catch (Exception ex) {
/* 116 */      if (LOG.isWarnEnabled()) LOG.warn(ex.toString(), ex);
/* 117 */       throw new CommonConfigException("Unable to open/parse configuration file: " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Document loadDocument(String name)
/*     */   {
/*     */     try
/*     */     {
/* 133 */       InputStream is = null;
/* 134 */       if (this.fileURL == null)
/*     */       {
				System.out.println("In loadDocument : "+name);
/* 136 */         ClassLoader cl = getClass().getClassLoader();
				  System.out.println("In ClassLoader : "+cl.toString());
/* 137 */         this.fileURL = cl.getResource(name);
/*     */ 		  System.out.println("In getResource : "+fileURL);
/* 139 */         File _fl = new File(this.fileURL.getFile());
/*     */ 		  System.out.println("In _fl : " + _fl);
/* 140 */         if (this.fileURL == null) {
/* 141 */           throw new CommonConfigException("Configuration file '" + name + "' can't be found on the CLASSPATH");
/*     */         }
/*     */       }
/*     */ 		
/* 145 */       is = this.fileURL.openStream();
/*     */ 		System.out.println("In openStream is : " + is);
/* 149 */       DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
/* 150 */       builderFactory.setValidating(false);
/*     */ 
/* 152 */       DocumentBuilder builder = builderFactory.newDocumentBuilder();
/*     */ 
/* 154 */       Document document = builder.parse(new InputSource(is));
/*     */       try
/*     */       {
/* 157 */         is.close();
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*     */       }
/*     */ 
/* 163 */       return document;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 167 */       throw new CommonConfigException("Unable to open/parse configuration file: " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected File getFile()
/*     */   {
/* 178 */     if (this.fileURL == null) {
/* 179 */       throw new IllegalStateException("XMLConfigLoaderAA::getFile(): file should be loaded first!");
/*     */     }
/*     */ 
/* 182 */     return new File(this.fileURL.getFile());
/*     */   }
/*     */ }

/* Location:           D:\mp_common.jar
 * Qualified Name:     com.dstm.common.util.XMLConfigLoaderAA
 * JD-Core Version:    0.6.2
 */