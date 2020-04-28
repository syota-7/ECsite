package com.example.ecsite.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//entity(DBに登録、更新する値を入れたり、DBから取得した値を保持する)なクラス
@Entity
//name="名前"のSQLテーブルとクラスを関連させる
@Table(name="user")
public class User {
	
		//プライマリーキーとなるプロパティの指定
		@Id
		//テーブルのname="名前"のカラムとプロパティを関連させる
		@Column(name="id")
		//プライマリキーカラムにユニークな値を自動で生成，付与する方法を指定
		//strategy属性はentityクラスのプライマリーキー値の生成方法を指定
		//GenerationType.IDENTITYはauto_increment列(自動採番するカラム)を利用して値を生成
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private long id;
		
		@Column(name="user_name")
		private String userName;
		
		@Column(name="password")
		private String password;
		
		@Column(name="full_name")
		private String fullName;
		
		//真偽の判断用データにはisを前につける
		@Column(name="is_admin")
		private int isAdmin;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

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

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public int getIsAdmin() {
			return isAdmin;
		}

		public void setIsAdmin(int isAdmin) {
			this.isAdmin = isAdmin;
		}
		

}
