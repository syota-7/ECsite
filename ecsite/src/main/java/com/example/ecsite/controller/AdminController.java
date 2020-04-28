package com.example.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ecsite.model.dao.GoodsRepository;
import com.example.ecsite.model.dao.UserRepository;
import com.example.ecsite.model.entity.Goods;
import com.example.ecsite.model.entity.User;
import com.example.ecsite.model.form.GoodsForm;
import com.example.ecsite.model.form.LoginForm;

@Controller
//()に記載のアドレスでこのクラスのメソッドにアクセス
@RequestMapping("/ecsite/admin")
public class AdminController {
	
	//AutowiredでインターフェースuserReposにspringFrameworkにより
	//実装クラスのオブジェクトが注入(利用できるようにする)される。(依存性の注入)
	@Autowired
	private UserRepository userRepos;
	
	@Autowired
	private GoodsRepository goodsRepos;
	
	//adminindexに遷移させるメソッド
	@RequestMapping("/")
	public String index() {
		return "adminindex";
	}
	
	//method=postでwelcome宛てに送られてきたフォームの処理を行う
	@PostMapping("/welcome")
	//引数に自作のモデルクラスを受け取る(LoginForm)
	//LoginFormオブジェクトにはuserNameやpasswordのパラメータが自動的に割り振られる
	//本来ModelAttributeが必要だが省略可能。その場合はクラスの先頭を小文字にした
	//ものがModelに追加される値の名となる(loginForm)
	public String welcome(LoginForm form,Model m) {
		//入力された名前とパスワードからテーブルで一致するレコードを取り出しUser型リストに代入
		List<User> users=userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		if(users != null && users.size()>0) {
			//真偽値変数に管理者であるかどうかの判定を行う
			boolean isAdmin=users.get(0).getIsAdmin() != 0;
			if(isAdmin) {
				//findAllメソッドでgoodsテーブルの全情報を取得
				List<Goods> goods=goodsRepos.findAll();
				//Modelに管理者の名前、パスワード、goodsテーブル情報を格納
				m.addAttribute("userName",users.get(0).getUserName());
				m.addAttribute("password", users.get(0).getPassword());
				m.addAttribute("goods",goods);
			}
		}
		
		return "welcome";
	}
	
	@RequestMapping("/goodsMst")
	//LoginFormインスタンスの中に送信されてきた値の名前と一致するものがあれば代入
	//htmlのフォームのnameが異なっていてもプロパティが対応していればよい
	public String goodsMst(LoginForm form, Model m) {
		m.addAttribute("userName", form.getUserName());
		m.addAttribute("password", form.getPassword());
		
		return "goodsMst";
	}
	
	@RequestMapping("/addGoods")
	//リクエストメソッドにルールはなく必要な値を引数にして、必要な値を戻り値とできる。
	//その際、引数には対応するプロパティを持つクラスを設定する
	public String addGoods(GoodsForm goodsForm,LoginForm form,Model m) {
		m.addAttribute("userName", form.getUserName());
		m.addAttribute("password", form.getPassword());
		
		//goodsインスタンスを生成してgoodsFormuに代入されたフォーム内容を
		//インスタンスにセットする。
		//saveAndFlushは引数のEntity(インスタンス)を保存してデータベースに反映させる。
		Goods goods=new Goods();
		goods.setGoodsName(goodsForm.getGoodsName());
		goods.setPrice(goodsForm.getPrice());
		goodsRepos.saveAndFlush(goods);
		
		//フォワードは同じアプリケーション内で直接処理を渡す方法
		return "forward:/ecsite/admin/welcome";
	}
	
	//レスポンスボディは次の処理への遷移を行わず戻り値がそのままhtmlに表示される
	@ResponseBody
	@PostMapping("/api/deleteGoods")
	//RequstBodyをつけることでjson形式でデータを受け取れという指示を行う
	public String deleteApi(@RequestBody GoodsForm f,Model m) {
		try {
			//取得したgoodsEntityに対応したIDのレコードを消去する
			goodsRepos.deleteById(f.getId());
		} catch(IllegalArgumentException e) {
			//削除しようとしたIDが存在しないときブラウザに-1を返す
			return "-1";
		}
		//ブラウザに1を返す
		return "1";
	}
	
}
