/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package model;

/**
 * This class will keep the server connection related information, 
 * @author asharma
 *
 */
public class JmxHost {
	private String host;
	private String port;
	
	public JmxHost(String host,String port){
		this.host = host;
		this.port = port;
	}
	/**
	 * 
	 * @return
	 */
	public String getHost() {
		return host;
	}
	/**
	 * 
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * 
	 * @return
	 */
	public String getPort() {
		return port;
	}
	/**
	 * 
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}
}
