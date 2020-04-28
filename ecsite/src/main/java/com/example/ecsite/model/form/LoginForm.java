package com.example.ecsite.model.form;

import java.io.Serializable;

//Stream経由でインスタンス情報をハードディスクやネットワークに送受信できたりする。
//Serializable実装クラスはオブジェクト情報をファイルに読み書きできる(バイト配列)。
public class LoginForm implements Serializable {
	
	//オブジェクトの読み書き前後でクラスのバージョンが異なっていないかを識別する為のものです。
	//なので、クラスのメンバに変更があればこの数値も変更する必要があります。
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
