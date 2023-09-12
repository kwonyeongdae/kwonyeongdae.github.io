---
title:  "첫번째 팀 프로젝트 - 거래/교환/환불 페이지"
excerpt: "아동용 책판매 쇼핑몰 - 거래/교환/환불"

categories:
  - team preject
tags:
  - java
  - spring
  - jsp
  - MySQL
last_modified_at: 2023-08-29 20:41PM
---
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발 기간</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 2023-07 ~ 2023-09(3개월)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">플랫폼</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Web</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발인원</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">3명(팀장)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">담당 역할</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 거래/교환/환불 기능(기여도 100%) 프론트엔드 -jsp(기여도 100%)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold; ">전체 코드&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><a href="https://github.com/kwonyeongdae/portfolio/tree/main/Login" style="text-decoration: none;"> github 보러가기(mypage-jsp참조)</a></div>
----------------------------------------------------
<h3>프로젝트</h3>

<div style="font-size : 17px; color:red; margin-bottom: 15px">책페이지와 연관되어 있음</div>
- 교환기능

<div style="font-size : 17px; color:blue; margin-bottom: 7px">FairyMapper.xml</div>
```html
<!-- 1. 교환 반품창에 보여줄 객체를 불러옵니다 -->
<select id="getchange" resultType="com.ezen.spring.board.teampro.cart.BookVO">
  SELECT * FROM boughtbook WHERE num = #{num}
</select>

```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 마이페이지에서 구매한 책 오른쪽에 교환/환불 버튼을 누르면 실행됩니다.</div>


<div style="font-size : 17px; color:blue; margin-bottom: 7px">CartController</div>
```java
@Controller
@RequestMapping("/fairy")
@SessionAttributes("userid")
public class FairyController 
{

  @Autowired
  @Qualifier("cartdao")
  CartDAO dao;

  @Autowired
  @Qualifier("fairydao")
  private FairyDAO fairydao;
  
  @GetMapping("/cart/change/{num}")
   public String change(@PathVariable int num,Model model,@SessionAttribute(name = "userid", required = false) String userid)
	{	
	    model.addAttribute("member",fairydao.getJoinedMem(userid));
	    model.addAttribute("book",dao.boughtget(num));
	   
    	    return "cart/changegoods";
	}
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 교환/환불창으로 이동합니다.
<img src="/assets/images/chan.png" width="800px" height="450px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">changegoods.jsp</div>
```html
<script type="text/javascript">
function clickTrans(num){
	if(!confirm("교환/반품을 신청하시겠습니까?"))return;
	 
	 var obj = {};
		obj.num = num;
		
	$.ajax({
		url:'/fairy/cart/changebook',
		method:'post',
		data: obj,
		cache:false,
		dataType:'json',
		success:function(res) {
			if (res.point) {
				location.href = "/fairy/mypage/${userid}";
		}	
		},
		error:function(xhr,status,err){
			alert(status + "/" + err);
		}
	});
}
</script>
<meta charset="utf-8">
<title>교환/반품</title>
<h1>교환/반품</h1><hr>
</head>
<body>
<div class="center-table">
<div id="a1"><img src="/img/${book.cvrimg}" style="max-width: 200px; max-height: 300px;"></div><br>
<div>제목 : ${book.bname}</div><br>
<div>가격 : <fmt:formatNumber value="${book.price}"/></div><br>
<div>수량 : ${book.quantity}</div><br>
<div id ="a2"></div>
</div>

<table>
<tr>
        <td class="info-label">아이디:</td>
        <td>${member.userid}</td>
      </tr>
      <tr>
        <td class="info-label">이름:</td>
        <td>${member.name}</td>
      </tr>
      <tr>
        <td class="info-label">휴대전화:</td>
        <td>${member.phone}</td>
      </tr>
      <tr>
        <td class="info-label">주소:</td>
        <td>${member.contact_address}&nbsp; ${member.detailed_address}</td>
      </tr>
<tr>
<td>교환/반품</td>
<td>
<label><input type ="radio" name = "change" value ="a">교환</label>
 <label><input type ="radio" name = "change" value ="b">반품</label>  
</select>
</td>
</tr>
<tr>
<button type="button" onclick ="clickTrans(${book.num})">신청</button>
 ```
<div style = "font-size : 15px; margin-bottom: 15px">
1. input -'radio'를 이용해서 교환인지 반품인지가 선택이 가능합니다.
<img src="/assets/images/ch1.png" width="800px" height="400px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">CartMapper.xml</div>
```html
<delete id="changebook" parameterType="com.ezen.spring.board.teampro.cart.BookVO">
  DELETE FROM test.boughtbook WHERE num = #{num}
</delete>
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 만약 교환/반품을하게 된다면 데이터베이스에 저장된 해당 결제항목은 삭제됩니다.
</div>
-----------------------------------------------------------------------------

- 타이머 버튼 기능

<div style="font-size : 17px; color:blue; margin-bottom: 7px">FairyMapper.xml</div>
```html
<!-- 1. 새책을 구매후 만족 했을경우 -->
<update id="OkBuy" parameterType="com.ezen.spring.board.teampro.cart.BookVO">
UPDATE test.boughtbook SET statu = 1 WHERE num = #{num}
</update>
<!-- 2. 중고물건을 거래완료 했을경우 -->
<update id="pointup" parameterType="com.ezen.spring.board.teampro.login.MemberVO">
	UPDATE fairymem SET carrotpoint = carrotpoint + #{carrotpoint} WHERE userid=#{uid}
</update>

<update id="Laststate" parameterType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO">
	UPDATE carrot SET state = '판매완료' WHERE userid = #{uid} and cnum = #{cnum}
</update>

<update id="boughtupdata" parameterType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO">
	UPDATE boughtcarrot SET state = 1 WHERE buyuserid = #{userid} and cnum = #{cnum}
</update>

<update id="up_state" parameterType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO">
	UPDATE interest SET state = '판매완료' WHERE cnum = #{cnum}
</update>

```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 새책과 중고물건을 구매 할 경우의  SQL문입니다.</div>


<div style="font-size : 17px; color:blue; margin-bottom: 7px">DAO와 Controller</div>
```java
   
   // 새책을 구매할 경우
   public boolean okbuy(int num) {
	return CartMapper.OkBuy(num)>0;
	}

   @PostMapping("/cart/ok")
   @ResponseBody
    public Map<String, Boolean> Ok_buy(@RequestParam int num){
		Map<String, Boolean> map = new HashMap<>();
		map.put("ok", dao.okbuy(num));
		
		return map;
	}

   // 중고물건을 구매했을경우새책을 구매할 경우
   @Transactional
    public boolean Trade(int carrotpoint,String userid,int cnum,String uid) 
	{
 	   boolean Laststate = fairyMapper.Laststate(uid,cnum)>0;
 	   boolean pointup = fairyMapper.pointup(carrotpoint, uid)>0;
 	   boolean state = fairyMapper.boughtupdata(userid, cnum)>0;
 	   boolean int_state = fairyMapper.up_state(cnum)>0;
    	   
	   return Laststate&&pointup&&state&&int_state;
        }

   @PostMapping("trade")
   @ResponseBody
    public Map<String,Boolean> Tradecarrot(@SessionAttribute(name = "userid", required = false)String userid,
		  			   @RequestParam int cnum,@RequestParam  int carrotpoint,@RequestParam String uid){
	  Map<String,Boolean> map = new HashMap<>();
	  map.put("tra",fairydao.Trade(carrotpoint,userid,cnum,uid));
	  
	  return map;
  }

```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 중고책은 구매자의 확인이 필요하므로 결제후 바로 포인트와 구매내역이 등록되지 않습니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">mypage.jsp</div>
```html
function tradehid(cnum,carrotpoint,uid){

    var obj = {
         'userid' : '${userid}',
         'cnum' : cnum,
         'carrotpoint' : carrotpoint,
         'uid' : uid
	};
    $.ajax({
	url:'/fairy/trade',
	method:'post',
	data: obj,
	cache:false,
	dataType:'json',
	success:function(res) {
		},
	error:function(xhr,status,err){
		alert(status + "/" + err);
		}
	});
    
}

	
function okbuy(num){

    var obj = {
         'num' : num,
	};
    
    $.ajax({
	url:'/fairy/cart/ok',
	method:'post',
	data: obj,
	cache:false,
	dataType:'json',
	success:function(res) {
		},
	error:function(xhr,status,err){
		alert(status + "/" + err);
		}
	});
    
}
<!-- 새책을 구매할 경우 -->
<button type="button" id="changeButton" onclick="clickTrans(${book.num})">교환/환불</button>
<button type="button" id="okButton" onclick="okbuy(${book.num})" style="display: none;"></button>

<!-- 중고책을 구매할 경우 -->
<button type="button" id="tradeButton" onclick="trade(${carrot.cnum},${carrot.price*0.9},'${carrot.saleuserid }')">
거래완료</button>
<button type="button" id="tradehid" onclick="tradehid(${carrot.cnum},${carrot.price*0.9},'${carrot.saleuserid }')" style="display: none;"></button>

<!-- 이용자가 버튼을 안누를시 7일후 자동으로 새책,중고책 모두 거래완료로 바뀜 -->
function simulateOkButtonClick() {
    var okButton = document.getElementById('okButton');
    okButton.click(); // 클릭 동작을 수행
}
document.addEventListener('DOMContentLoaded', function() {
    setTimeout(simulateOkButtonClick, 7 * 24 * 60 * 60 * 1000);//7일 뒤 수령하는로직
});
</script>

<script type="text/javascript">
function simulateTradehidClick() {
    var tradehid = document.getElementById('tradehid');
    tradehid.click(); // 클릭 동작을 수행
}
document.addEventListener('DOMContentLoaded', function() {
    setTimeout(simulateTradehidClick, 7 * 24 * 60 * 60 * 1000);//7일뒤 거래완료로 바뀜
});
          	  
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 이용자 화면에는 교환/환불 버튼과 거래완료 버튼만 보입니다.
<img src="/assets/images/okt.png" width="550px" height="350px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
2. 보통의 이용자들은 교환/환불이 아닌경우 구매확정 버튼을 누르지 않기 때문에 7일후 자동으로 버튼을 클릭하는 로직을 만들었습니다.
</div>

<div style = "font-size : 20px; color:red; margin-bottom: 15px">
END
</div>
-----------------------------------------------------------------------------
<h3>개발 환경</h3>
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">언어</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Java(JDK17), HTML/CSS/JSP, JavaScript, Python</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">서버</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Apache Tomcat10.1</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">프레임워크</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Spring Boot,MyBatis,ORM</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">DB</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">MySQL8.0</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">IDE</span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Eclipse</span></div>

<div style ="margin-bottom: 10px; font-size : 15px; "><span style="font-weight: bold;">API, 라이브러리</span>&nbsp;&nbsp;<span style="color:gray;">Jquery, Maven</span></div>
