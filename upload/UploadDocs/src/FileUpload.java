import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.callback.LanguageCallback;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.tools.ant.BuildException;
import org.exolab.castor.xml.Unmarshaller;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.w3c.dom.Element;

import com.dstm.common.util.XMLConfigLoader;
import com.dstm.common.util.JSLocalize;
import com.dstm.common.tools.deploy.ewsconf.*;
import com.dstm.common.log.DSTMLogger;

import java.net.URL;

import com.dstm.common.util.*;

public class FileUpload {

	
	private static final DSTMLogger LOG = DSTMLogger.getLogger(FileUpload.class);
	private String tenant;
	private String dir;
	private String lang;
	public URL fileURL;
	
	public URL getFileURL()
	{
	   return fileURL;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public void convertFileToBytes(String pFileLocation, String pTenantName)
			throws SQLException, IOException, ClassNotFoundException {
		Connection vConnection = null;
		FileInputStream vFileInputStream = null;
		ByteArrayOutputStream vBaos = null;
		ResultSet vRs = null;
		PreparedStatement vStatement = null;
		
		try {
			
			//FileUpload vFileUpload = new FileUpload();
			//vFileUpload.execute();
			//LanguageJsGenerator vLanguageJsGenerator = new LanguageJsGenerator();
			//vLanguageJsGenerator.execute();
			String Vquery = "Select DISTINCT(DocumentLocation) From [dbo].[AR_Document] where Filedata IS NULL";
			vStatement = vConnection.prepareStatement(Vquery);
			vRs = vStatement.executeQuery();
			System.out.println("vRs :" + vRs);
			List<String> vList = new ArrayList<String>();
			while (vRs.next()) {
				vList.add(vRs.getString(1));
			}
			
			for (String vFilelocation : vList) {
				String vServerFilePath = pFileLocation;
				try {
					vServerFilePath = vServerFilePath + "\\" + pTenantName+vFilelocation.trim();
					System.out.println("server path is :"+vServerFilePath);
					File vFilePath = new File(vServerFilePath);
					vFileInputStream = new FileInputStream(vFilePath);
					vBaos = new ByteArrayOutputStream();
					int vReads = vFileInputStream.read();
					while (vReads-- != -1) {
						vBaos.write(vReads);
					}
					Blob vBlob = new SerialBlob(vBaos.toByteArray());
					String sql = "UPDATE  [dbo].[AR_Document] SET FILEDATA = ? WHERE DocumentLocation= ? AND  Filedata IS NULL ";
					vStatement = vConnection.prepareStatement(sql);
					vStatement.setBlob(1, vBlob);
					vStatement.setString(2, vFilelocation);
					vStatement.executeUpdate();
					System.out.print("neeraj bali updated sucessfully");

				} catch (FileNotFoundException e) {
					LOG.info("FileNotFound Exception occured in File Upload process : "
							+ e);
					e.printStackTrace();
				} catch (IOException e) {
					LOG.info("Exception1 occured in File Upload process : " + e);
					e.printStackTrace();
				} finally {
					vFileInputStream.close();
					vBaos.close();
				}
			}
		} finally {
			vRs.close();
			vStatement.close();
			vConnection.close();
		}
	}

	public void execute() {
		EwsConfig config;
		try {
						 
			XMLConfigLoaderAA  _conf = XMLConfigLoaderAA.getInstance();
			config = (EwsConfig) _conf.loadToCastor("ewsconfig.xml",com.dstm.common.tools.deploy.ewsconf.EwsConfig.class);
				
		} catch (Throwable ex) {
			throw new BuildException("Failed to parser configuration file: "+ ex.getMessage());
		}
		
		// we will iterate through all data sources
		for (Enumeration itt = config.getDbConnections().enumerateDbConnection(); itt.hasMoreElements();) {
			DbConnection connDef = (DbConnection) itt.nextElement();
			
			try {
				if ((tenant.trim().equals("ALL"))
						|| tenant.trim().equals(connDef.getName())) {
					Class.forName(connDef.getDriver()).newInstance();
					String password = TxtObfuscat.decode(connDef.getPassword());
					Connection conn = DriverManager.getConnection(connDef.getUrl(),connDef.getUser(), password);
					JSLocalize genLang = new JSLocalize(conn, dir, connDef.getName(),lang);
					genLang.runTenant();
					LOG.info(" generated language files successfully for Tenant '" +connDef.getName() + "'!");
				}
			} catch (Exception ex) {
				LOG.info("Connection test for '" + connDef.getName() + "' FAILED: " +
				ex.getMessage());
				throw new BuildException("Datasource test failed!");
			}
		}
	}

	public static void main(String[] args) {

		FileUpload vFileUpload = new FileUpload();
		try {
			String vDocLocation = args[1];
			String vMpConfigTenantNames = args[2];
			String vEwsConfigTenantNames = args[3];
			boolean vtenantNamePresent = vMpConfigTenantNames.contains(vEwsConfigTenantNames);
			System.out.println("location parth is : "+ vDocLocation);
			System.out.println("vMpConfigTenantNames path is : "+ vMpConfigTenantNames);
			System.out.println("EwsConfigTenantNames path is : "+ vEwsConfigTenantNames);
			if (vtenantNamePresent) {
				vFileUpload.convertFileToBytes(vDocLocation, vEwsConfigTenantNames);
			}

		} catch (Exception e) {
			LOG.info("Exception main occured in File Upload process : " + e);
			e.printStackTrace();
		}
	}
}