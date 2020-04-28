package com.example.ecsite.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecsite.model.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
	
	//@Queryのカッコ内の"SERECT～"でfindByHistoryで実行される
	//クエリ文を指定している。下記のクエリ文は受け取ったuserIdで
	//テーブルが挿入された時間が一番遅いレコードを取得
	@Query(value="SELECT * FROM purchase p " +
			"WHERE created_at =(" +
			"SELECT MAX(created_at) FROM purchase p WHERE p.user_id = :userId)",
			//この記述でJPQLではなくSQLで実行できる
			nativeQuery=true)
	//@Paramのカッコ内の"userId"で@Queryのカッコ内の"userId"=クライアント側
	//のデータを示している。メソッドが実行されると@ParamがクライアントのuserIdを受け取る
	//spring Date JPAでは位置ではなく名前で判別するパラメータを使用できる
	//クエリーの中では:名前とすることで使用可能
	List<Purchase> findHistory(@Param("userId") long userId);
	
	@Query(value="INSERT INTO purchase (user_id, goods_id, goods_name, item_count, total, created_at)" +
			"VALUES (?1, ?2, ?3, ?4, ?5, now())",nativeQuery=true)
	//Transactionalアノテーションは対応したメソッドでトランザクション処理を行う
	//具体的にはメソッドが正常に動作したらコミット(確定)、例外で終了したら処理前の状態に戻る
	@Transactional
	//メソッドがクエリ更新文(insert,update,delete)であることを明示するアノテーション
	@Modifying
	//Paramアノテーションの値が?の1-5まで順番に代入される
	void persist(@Param("userId") long userId,
				 @Param("goodsId") long productId,
				 @Param("goodsName") String goodsName,
				 @Param("itemCount") long itemCount,
				 @Param("total") long total);
}
