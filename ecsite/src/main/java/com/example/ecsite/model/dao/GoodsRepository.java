package com.example.ecsite.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecsite.model.entity.Goods;

//リポジトリとは、DBへのCRUDを自動で実装してくれる便利インターフェース
//JpaRepositoryを継承してクラス名の上にアノテーション指定
@Repository
//第一引数にはアクセスしたいEntityクラス名、第二引数にはテーブルID(プライマリーキー)の型を指定
public interface GoodsRepository extends JpaRepository<Goods,Long> {
	
}
