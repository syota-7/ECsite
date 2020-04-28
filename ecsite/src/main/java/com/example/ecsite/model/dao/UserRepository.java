package com.example.ecsite.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecsite.model.entity.User;

//リポジトリとは、DBへのCRUDを自動で実装してくれる便利インターフェース
//JpaRepositoryを継承してクラス名の上にアノテーション指定
@Repository
//第一引数にはアクセスしたいEntityクラス名、第二引数にはテーブルID(プライマリーキー)の型を指定
public interface UserRepository extends JpaRepository<User, Long> {
	
	//JpaRepositoryでは、あらかじめ決まった型式でメソッド名を書いておくと、
	//その名前を元にして実際に動くメソッドが自動生成される。
	//下記のメソッドはfindByの後にEntityのフィールドを記載すると引数に指定したものから
	//一致するものを探してくれるメソッド
	List<User> findByUserNameAndPassword(String userName,String password);

}
