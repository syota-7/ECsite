package com.example.ecsite.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ecsite.model.dao.GoodsRepository;
import com.example.ecsite.model.dao.PurchaseRepository;
import com.example.ecsite.model.dao.UserRepository;
import com.example.ecsite.model.dto.HistoryDto;
import com.example.ecsite.model.dto.LoginDto;
import com.example.ecsite.model.entity.Goods;
import com.example.ecsite.model.entity.Purchase;
import com.example.ecsite.model.entity.User;
import com.example.ecsite.model.form.CartForm;
import com.example.ecsite.model.form.HistoryForm;
import com.example.ecsite.model.form.LoginForm;
import com.google.gson.Gson;

//コントローラークラス
@Controller
//()内のアドレスから処理を行う
@RequestMapping("/ecsite")
public class IndexController {
	
	//参照先がgoodsからuserに変わっただけ
	@Autowired
	private UserRepository userRepos;
	
	//goodsReposにgoodsテーブル情報の入ったインスタンスを代入
	@Autowired
	private GoodsRepository goodsRepos;
	
	@Autowired
	private PurchaseRepository purchaseRepos;
	
	//このクラスをWebサービスAPIとするためレスポンスの形式として利用するGsonをインスタンス化
	private Gson gson=new Gson();
	
	//レスポンスボディは次の処理への遷移を行わず戻り値がそのままhtmlに表示される
	@ResponseBody
	@PostMapping("/api/login")
	//RequstBodyをつけることでjson形式でデータを受け取れという指示を行う
	public String loginApi(@RequestBody LoginForm form) {
		//フォームから受け取った名前とパスワードから登録されているユーザーを探してuserに代入
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(),form.getPassword());
		//中身の情報がゲストなdtoインスタンス生成(フォームからのユーザー情報)
		LoginDto dto=new LoginDto(0,null,null,"ゲスト");
		//適合するユーザー情報があれば対応したdtoインスタンスを生成
		if(users.size() > 0) {
			dto =new LoginDto(users.get(0));
		}
		//dtoオブジェクトをgson形式に変換
		return gson.toJson(dto);
	}
	
	//レスポンスボディは次の処理への遷移を行わず戻り値がそのままhtmlに表示される
	@ResponseBody
	@PostMapping("/api/purchase")
	public String purchaseApi(@RequestBody CartForm f) {
		//カート内の商品について一つずつラムダ式で繰り返し処理を行う
		//合計金額を計算して各情報をinsert文に送って購入レコードを作成
		f.getCartList().forEach((c) -> {
			long total = c.getPrice() * c.getCount();
			purchaseRepos.persist(f.getUserId(), c.getId(), c.getGoodsName(), c.getCount(), total);
		});
		
		//カートリストの商品数という数字を文字列に変換して返す(int->String)
		return String.valueOf(f.getCartList().size());
	}
	
	@ResponseBody
	@PostMapping("/api/history")
	public String historyApi(@RequestBody HistoryForm form) {
		String userId = form.getUserId();
		//idからselect文で購入レコードを抜き出して、テーブル型のリストに代入
		//また文字列の引数をlong型の数値として認識する
		List<Purchase> history = purchaseRepos.findHistory(Long.parseLong(userId));
		//HistoryDtoリストのインスタンスを生成
		List<HistoryDto> historyDtoList =new ArrayList<>();
		history.forEach((v) -> {
			//purchaseリストのhistoryをレコードの分だけ新たにインスタンスを生成してリストに代入
			HistoryDto dto = new HistoryDto(v);
			historyDtoList.add(dto);
		});
		
		//リストオブジェクトをgsonの形にして呼び出し元へ戻す
		return  gson.toJson(historyDtoList);
	} 
	
	@RequestMapping("/")
	//goodsテーブル情報のレコードを全てgoodsに代入
	public String index(Model m) {
		List<Goods> goods=goodsRepos.findAll();
		//Modelにgoodsテーブルを追加
		m.addAttribute("goods",goods);
		
		return "index";
	}

}
