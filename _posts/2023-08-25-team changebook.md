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
last_modified_at: 2023-08-28 19:23PM
---
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발 기간</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 2023-07 ~ 2023-09(3개월)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">플랫폼</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Web</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발인원</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">3명(팀장)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">담당 역할</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 거래/교환/환불 기능(기여도 100%) 프론트엔드 -jsp(기여도 100%)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold; ">전체 코드&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><a href="https://github.com/kwonyeongdae/portfolio/tree/main/Login" style="text-decoration: none;"> github 보러가기</a></div>
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

이상 거래/교환/환불 방식에 대해 알아보았습니다.
-----------------------------------------------------------------------------
<h3>개발 환경</h3>
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">언어</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Java(JDK17), HTML/CSS/JSP, JavaScript, Python</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">서버</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Apache Tomcat10.1</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">프레임워크</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Spring Boot,MyBatis</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">DB</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">MySQL8.0</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">IDE</span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Eclipse</span></div>

<div style ="margin-bottom: 10px; font-size : 15px; "><span style="font-weight: bold;">API, 라이브러리</span>&nbsp;&nbsp;<span style="color:gray;">Jquery, Maven</span></div>
