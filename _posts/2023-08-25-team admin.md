---
title:  "첫번째 팀 프로젝트 - 관리자 페이지"
excerpt: "아동용 책판매 쇼핑몰 - 관리자"

categories:
  - team preject
tags:
  - java
  - spring
  - jsp
  - MySQL
last_modified_at: 2023-08-30 15:03PM
---
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발 기간</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 2023-07 ~ 2023-09(3개월)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">플랫폼</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Web</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발인원</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">3명(팀장)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">담당 역할</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 관리자 페이지 개발(기여도 100%) 프론트엔드-jsp(기여도 100%)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold; ">전체 코드&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><a href="https://github.com/kwonyeongdae/portfolio/tree/main/Admin" style="text-decoration: none;"> github 보러가기</a></div>
----------------------------------------------------
<h3>프로젝트</h3>

- 직원 등록기능

<div style="font-size : 17px; color:blue; margin-bottom: 7px">AdminVO</div>
```java
@Component("adminvo")
public class AdminVO {

	private int num;
	private int bnum;
	//매출에 관한 검색기능을 이용할때 쓰는 컬럼
	private String year;
	private String month;
	private String day;

	private int total;
	private String bname;
	private String buyday;
	private String statu;
	private int price;
	private int quantity;
	private int totalquantity;
	private String userid;
	private String payment;
}
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 직원 등록 및 매출전표에 필요한 컬럼들만 넣었습니다.</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">AdminMapper.xml</div>
```html
<!-- 직원 계정 생성로직 -->
<insert id="adminadd"
	  parameterType="com.ezen.spring.board.teampro.login.MemberVO">
	  INSERT INTO fairymem VALUES(NULL, #{userid}, #{pass}, #{name}, #{phone}, #{email}, #{birth}, #{gender}, #{agrStipulation1}, #{agrStipulation2}, #{agrStipulation3},#{number},#{carrotpoint},0)
</insert>


```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 직원 등록계정은 회원가입SQL문을 그대로 가져왔습니다.</div>
<div style = "font-size : 15px; margin-bottom: 15px">
2. 회원가입시에는 일반회원 number가 0이지만 직원등록시에는 number가 9로 설정되어 있습니다.</div>


<div style="font-size : 17px; color:blue; margin-bottom: 7px">FairyController</div>
```java
@Controller
@RequestMapping("/admin")
@SessionAttributes("userid")
public class AdmainController  
{

  @Autowired
  @Qualifier("admindao")
  AdminDAO admindao;

  //어드민 계정으로 로그인 하면 어드민전용 창에서 직원아이디 생성해주는 로직
  @PostMapping("/add")
  @ResponseBody
   public Map<String,Boolean> adminjoin(MemberVO vo){
	  Map<String,Boolean> map = new HashMap<>();
	  map.put("added", admindao.addadmin(vo));
	  
	  return map;
	}
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 직원계정을 만들려면 어드민계정으로 로그인을 해야 가능합니다.</div>
<div style = "font-size : 15px; margin-bottom: 15px">
2. 예를들면 인사팀에서 입사한 직원의 계정을 만들어주는 것과 같습니다.</div>

- 매출보기

<div style="font-size : 17px; color:blue; margin-bottom: 7px">AdminMapper.xml</div>
```html
<!-- 총 매출액을 보여줍니다 -->
<select id="getlist" resultType="com.ezen.spring.board.teampro.admin.AdminVO" >
   SELECT quantity,beyday,bname, userid, price FROM boughtbook;
</select>

<!-- 일별 매출액을 보여줍니다 -->
<select id="getByDay" resultType="com.ezen.spring.board.teampro.admin.AdminVO" parameterType="String">
   SELECT DAY(beyday) AS day, bname, quantity, price,userid
   FROM boughtbook
   WHERE beyday = #{beyday}
   GROUP BY DAY(beyday), bname, quantity, price,userid
</select>

<!-- 월별 매출액을 보여줍니다 -->    
<select id="getByMonth" resultType="com.ezen.spring.board.teampro.admin.AdminVO" parameterType="String">
   SELECT MONTH(beyday) AS month,DAY(beyday) AS day, bname, quantity, price, SUM(price*quantity) AS total,userid
   FROM boughtbook
   WHERE YEAR(beyday) = #{year}
    AND MONTH(beyday) = #{month}
   GROUP BY MONTH(beyday), DAY(beyday), bname, quantity, price,userid
</select>
<!-- 연별 매출액을 보여줍니다 -->  
<select id="getByYear" resultType="com.ezen.spring.board.teampro.admin.AdminVO" parameterType="String">
   SELECT MONTH(beyday) AS month,YEAR(beyday) AS year,
   SUM(quantity) AS totalquantity,
   SUM(price*quantity) AS total
	FROM boughtbook
	WHERE YEAR(beyday) = #{year}
	GROUP BY MONTH(beyday),YEAR(beyday);
</select>
<!-- 이용자별 매출액을 보여줍니다 -->  
<select id="getByid" resultType="com.ezen.spring.board.teampro.admin.AdminVO" parameterType="String">
   SELECT beyday, bname, quantity, price, userid,
   SUM(price*quantity) AS total
	FROM boughtbook
	WHERE userid = #{userid}
	GROUP BY beyday, bname, quantity, price,userid
</select>

```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 일별, 월별, 년도별, 아이디별로 매출액을 가져옵니다.</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">FairyController</div>
```java

  // 이용자의 정보를 받아오는 메소드를 만들어 보았습니다.
  public Model mod(Model model,@SessionAttribute(name = "userid", required = false)String userid) {
	return model.addAttribute("user",admindao.joinmem(userid));
	}
  // 총 매출액을 보여줌
  @GetMapping("/list")
   public String getlist(Model model,@SessionAttribute(name = "userid", required = false)String userid) 
	{  
	  mod(model,userid);
	  
	  List<AdminVO> alist = new ArrayList<>();
	  alist = admindao.getBylist();
		
 	  model.addAttribute("alist",alist);
	
	  return "admin/Adminlist";
	}
  // 년, 월, 일, 아이디별로 검색이 가능합니다. 
  @GetMapping("/list/data")
   public String getday(@RequestParam("category") String category, @RequestParam("key") String key,Model model) 
	   {
		List<AdminVO> daylist = new ArrayList<>();
		if (category.equals("day")) {
		// 일별 데이터 조회를 위한 SQL 쿼리 실행
		daylist = admindao.getByDay(key);
	    } else if (category.equals("month")) {
	        // 월별 데이터 조회를 위한 SQL 쿼리 실행
	    	 String year = key.substring(0, 4);
	    	 String month = key.substring(5, 7);
	    	daylist = admindao.getByMonth(year,month);
	    } else if (category.equals("year")) {
	        // 연도별 데이터 조회를 위한 SQL 쿼리 실행
	    	daylist = admindao.getByYear(key);
	    } else if (category.equals("uid")) {
	        // 연도별 데이터 조회를 위한 SQL 쿼리 실행
	    	daylist = admindao.getById(key);
	    }
		model.addAttribute("daylist",daylist);
		return "admin/Adminlist";
	}

```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 월별 조회시 substring은 자릿수를 의미 합니다 예를 들면 2023-08 년4자리 월2자리로 입력을받을수 있습니다.
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
2. 예를 들면 2023-08 년4자리 월2자리로 입력을받을수 있습니다.
</div>
```html
<!-- 코드의 이해를 돕기위해  필요 없는 코드는 뺏습니다 -->
<!-- 일별 매출 출력 -->
<c:choose>
   <c:when test="${param.category eq 'day'}">
      <h1>${daylist[0].day}일 매출액</h1>
      <form id="day" action="/admin/list/data" method="get">
       
      </form>
   </c:when>
</c:choose>

<!-- 월별 매출 출력 -->
<c:choose>
   <c:when test="${param.category eq 'month'}">
      <h1>${daylist[0].month}월 매출액</h1>
      <form id="day" action="/admin/list/data" method="get">
        
      </form>
   </c:when>
</c:choose>

<!-- 연별 매출 출력 -->
<c:choose>
   <c:when test="${param.category eq 'year'}">
      <h1>${daylist[0].year}년도 매출액</h1>
      <form id="day" action="/admin/list/data" method="get">
         
      </form>
   </c:when>
</c:choose>

<!-- 이용자 매출 출력 -->
<c:choose>
   <c:when test="${param.category eq 'uid'}">
      <h1>${daylist[0].uid}님의 총구매액</h1>
      <form id="day" action="/admin/list/data" method="get">
      </form>
   </c:when>
</c:choose>

<!-- 검색기능 -->
<form id="div" action="/admin/list/data" method="get">
   <select id="category" name="category">
    <option value="day">일</option>
    <option value="month">월</option>
    <option value="year">연도</option>
    <option value="uid">아이디</option>
</select>
<input type="text" name="key">
<input type="submit" value="검색">
<div class="ca">**검색예시**</div>

<!-- 검색기능 사용시 안내문구 -->
<script>
    var categorySelect = document.getElementById("category");
    var caDiv = document.querySelector(".ca");

    categorySelect.addEventListener("change", function() {
        var selectedOption = categorySelect.value;
        var exampleText = "";

        if (selectedOption === "day") {
            exampleText = "2023-01-01";
        } else if (selectedOption === "month") {
            exampleText = "2023-01";
        } else if (selectedOption === "year") {
            exampleText = "2023";
        }else if (selectedOption === "uid") {
            exampleText = "아이디를 입력하세요";
        }

        caDiv.textContent = "ex) " + exampleText;
        caDiv.style.color = "red";
    });
</script>
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 총 매출액과 검색별로 나오는 매출액은 한개의 jsp에서 출력됩니다.</div>

<div style = "font-size : 15px; margin-bottom: 15px">
2. 모든 입력란을 채우면 회원가입이 됩니다.
<img src="/assets/images/day.png" width="800px" height="300px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
<img src="/assets/images/month.png" width="800px" height="300px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
<img src="/assets/images/year.png" width="800px" height="300px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
<img src="/assets/images/ids.png" width="800px" height="300px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
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
