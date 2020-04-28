//変数ログインに引数eventを設定した関数を代入
let login =(event) => {
	
	//デフォルトの動作を発生させない。
	//この場合はactionで指定されたURLへ遷移＋データ送信をさせない
	event.preventDefault();
	
	//val()は対象タグのvalue値を取得する
	//フォームの対応するタグに入力された値をオブジェクトとして変数に代入
	let jsonString={
			'userName': $('input[name=userName]').val(),
			'password': $('input[name=password]').val()
	};
	//ajaxメソッドの記述。非同期でページ更新をする技術
	$.ajax({
		type: 'POST', //使用するHTTPメソッド
		url: '/ecsite/api/login', //送信先のurl
		// 実際に送信するデータを指定。下記はオブジェクトをJSON形式に変換している
		data: JSON.stringify(jsonString),
		contentType: 'application/json', //サーバーに送信する型の指定
		datatype: 'json', //サーバーから受け取るデータの型を指定
		scriptCharset: 'utf-8' //データを特定の文字コードに指定
	})
	//　ajaxメソッドはXMLHTTPRequestオブジェクトを返しその中のPromiseオブジェクトの
	//　thenというメソッドを使用。thenは非同期実行が成功したときとそうでないときの２つの引数を持つ
	//非同期実行が成功したら値(value)を受け取るメソッド
	.then((result) =>{
			//apiから受け取ったデータをJSONからオブジェクトに変換
			let user = JSON.parse(result);
			//idがwelcomeのタグテキストを更新
			$('#welcome').text(` -- ようこそ！　${user.fullName} さん`);
			//隠しているinputにidを代入、また入力部分を空白にする
			$('#hiddenUserId').val(user.id);
			$('input[name=userName]').val('');
			$('input[name=password]').val('');
		},() => {
			console.error('Error: ajax connection failed.');
		}
	);
};

//変数ログインに引数eventを設定した関数を代入
let addCart = (event) => {
	
	//event.targetはイベント発生元のオブジェクトを参照する
	//さらにparent()メソッドで2段階上の親要素を取得
	//その後find()メソッドで()内に指定された子、孫要素を取得
	//find()メソッドの戻り値は配列となる
	let tdList = $(event.target).parent().parent().find('td');
	
	//取得した値は配列になっているのでそれぞれ変数に代入
	let id=$(tdList[0]).text();
	let goodsName = $(tdList[1]).text();
	let price = $(tdList[2]).text();
	//td内のinputに入力されている数を代入
	let count = $(tdList[3]).find('input').val();
	
	//入力されている値が下記の条件に該当していたらアラートを表示して処理を中断する
	if(count === '0' || count === ''){
		alert('注文数が０または空欄です。')
		return;
	}
	
	//取得した値をそれぞれプロパティの値に設定してオブジェクトとして変数に代入
	let cart = {
		'id': id,
		'goodsName': goodsName,
		'price': price,
		'count': count
	};
	//配列にオブジェクトを追加
	cartList.push(cart);
	//tbody変数にidがcartタグのtbody要素を代入
	let tbody = $('#cart').find('tbody');
	//tbodyの子要素タグを削除
	$(tbody).children().remove();
	
	//配列cartListの繰り返し処理を行う
	//コールバック関数として配列の中身(cart)とインデックス番号を引数に持つ
	cartList.forEach(function(cart,index){
		
		//trタグオブジェクトを変数に代入
		let tr=$('<tr />');
		//textがcartの値なtdタグオブジェクトをtrタグに追加
		$('<td />', { 'text': cart.id}).appendTo(tr);
		$('<td />', { 'text': cart.goodsName}).appendTo(tr);
		$('<td />', { 'text': cart.price}).appendTo(tr);
		$('<td />', { 'text': cart.count}).appendTo(tr);
		
		let tdButton = $('<td />');
		//textがカート削除、classがremoveBtnなボタンタグオブジェクトをtdタグに追加
		$('<button />',{
			'text': 'カート削除',
			'class':'removeBtn',
		}).appendTo(tdButton);
		//ボタンタグの入ったtdタグをtrタグに追加
		$(tdButton).appendTo(tr);
		//trタグをtbodyに追加
		$(tr).appendTo(tbody);
	});
	$('.removeBtn').on('click', removeCart);
};

let buy = (event) => {
	$.ajax({
		type: 'POST',
		url: "/ecsite/api/purchase",
		data: JSON.stringify({
			//ログイン時に代入されたvalue値のidを取得
			"userId": $('#hiddenUserId').val(),
			//カートリストオブジェクト
			"cartList": cartList
		}),
		contentType: 'application/json',
		datatype: 'json',
		scriptCharset: 'utf-8'
	})
	.then((result) => {
		//成功時の処理
		alert('購入しました。');
		},() => {
		//失敗時の処理
		console.error('Error: ajax connection failed.');
		}
	);

};

let removeCart = (event) =>{
	//constは再代入、再宣言禁止の定数
	const tdList = $(event.target).parent().parent().find('td');
	//tdの中のidの値を取得
	let id=$(tdList[0]).text();
	//filterメソッドでcartListの中身を削除する商品のid以外の商品を配列として返す
	cartList = cartList.filter(function(cart){
		return cart.id !== id;
	});
	//削除対象のtbodyを削除
	$(event.target).parent().parent().remove();
};

let showHistory = () => {
	$.ajax({
		type: 'POST',
		url: '/ecsite/api/history',
		//javascriptオブジェクトをjson文字列に変換
		data: JSON.stringify({ "userId": $('#hiddenUserId').val() }),
		contentType: 'application/json',
		datatype: 'json',
		scriptCharset: 'utf-8'
	})
	.then((result) => {
			//返されてきたjsonをjavascriptオブジェクトに変換
			let historyList = JSON.parse(result);
			//idがhistoryTableのtbodyを取得
			let tbody = $('#historyTable').find('tbody');
			$(tbody).children().remove();
			//コールバック関数として配列の中身(history)とインデックス番号を引数に持つ
			historyList.forEach((history, index) => {
				
				let tr = $('<tr />');
				//textがhistoryの値なtdタグオブジェクトをtrタグに追加
				$('<td />', { 'text': history.goodsName }).appendTo(tr);
				$('<td />', { 'text': history.itemCount }).appendTo(tr);
				$('<td />', { 'text': history.createdAt }).appendTo(tr);
				//trタグをtbodyに追加
				$(tr).appendTo(tbody);
			});
			//idがhistoryのダイアログを開く
			$("#history").dialog("open");
		}, () => {
			//ajax送信が失敗したとき
			console.error('Error: ajax connection failed.');
		}
	);
}

