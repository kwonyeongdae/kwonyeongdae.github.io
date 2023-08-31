---
title:  "첫번째 팀 프로젝트 - 판매자간 C2C 페이지"
excerpt: "아동용 책판매 쇼핑몰 - 판매자간 C2C"

categories:
  - personal project
tags:
  - java
  - spring
  - html
  - Thymeleaf
  - MySQL
last_modified_at: 2023-08-31 19:14PM
---
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발 기간</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 2023-07 ~ 2023-09(3개월)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">플랫폼</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Web</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발인원</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">3명(팀장)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">담당 역할</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 판매자간 C2C 페이지 개발(기여도 100%) 프론트엔드-html(기여도 100%)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold; ">전체 코드&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><a href="https://github.com/kwonyeongdae/portfolio/tree/main/Carrot" style="text-decoration: none;"> github 보러가기</a></div>
----------------------------------------------------
<h3>프로젝트</h3>

- 회원가입기능

<div style="font-size : 17px; color:blue; margin-bottom: 7px">MemberVO</div>
```java
public class CarrotVO {

	private int cnum;
	private String userid;
	private String saleuserid;
	private String cvrimg; 
	private float imgsize;
	private String imgtype;
	private int price;
	private String title;
	private String content; 
	private java.util.Date cdata;
	private int hitnum;
	private int number;
	//
	private List<CarrotAttach> clist;
	private String state;
	private String payment;
}
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 중고거래 특성상 판매자(saleuserid) 아이디와 구매자(userid) 아이디를 모두 필요합니다.</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotMapper.xml</div>
```html

<!-- 중고물품 추가 -->
<insert id="addcarrot" parameterType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO">
<selectKey keyProperty="cnum" resultType="int" order="AFTER">
	      SELECT LAST_INSERT_ID()
	   	</selectKey>
INSERT INTO carrot
VALUES(NULL,#{userid},#{cvrimg},#{imgsize},#{imgtype},#{price}, #{title}, #{content},#{hitnum},#{state},#{number},CURRENT_TIMESTAMP)
</insert>
<!-- 중고물품 특성사진 추가 -->
<insert id="addcarrotfile" parameterType="com.ezen.spring.board.teampro.carrotmarket.CarrotAttach">
    INSERT INTO carrotattach (num, cnum, contimg1, contsize1, conttype1)
    VALUES
    <foreach collection="list" item="item" separator=", ">
        (NULL, #{item.cnum}, #{item.contimg1}, #{item.contsize1}, #{item.conttype1})
    </foreach>
</insert> 
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 중고이기 때문에 상세설명에는 하자가 있다면 사진이 필요합니다.</div>
<div style = "font-size : 15px; margin-bottom: 15px">
2. 그래서 두개의 테이블이 필요합니다.</div>


<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotDAO와CarrotController</div>
```java
@Component("carrotdao")
public class CarrotDAO {
	
	@Autowired
	@Qualifier("carrotmapper")
	CarrotMapper carrotmapper;
	
	public boolean addcarrotAll(CarrotVO cv) {
		
		boolean bSaved = carrotmapper.addcarrot(cv)>0;
		
		int cnum = cv.getCnum();
		List<CarrotAttach> list = cv.getClist();
		if (list != null && !list.isEmpty()) {
		    for (int i = 0; i < list.size(); i++) {
		         list.get(i).setCnum(cnum);
		        }
		    	boolean att = carrotmapper.addcarrotfile(cv.getClist())> 0;
			        if (!bSaved) {
			            return false;
			        }
		    }
		    return bSaved;
	}
}

@Controller
@RequestMapping("/carrot")
@SessionAttributes("userid")
public class CarrotController {

   @Autowired
   @Qualifier("carrotdao")
    CarrotDAO dao;

   @PostMapping("/add")
   @ResponseBody
    public Map<String, Object> addcarrot(@SessionAttribute(name = "userid", required = false)String userid,
    		   			 @RequestParam(value ="file1", required=false) MultipartFile cvrimg,
                                         @RequestParam(value ="file2", required=false) MultipartFile[] contimg1,
                                  	  CarrotVO cv,HttpServletRequest request) 
      {
		
      Map<String, Object> map = new HashMap<>();
      List<CarrotAttach> filelist = new ArrayList<>();
      ServletContext context = request.getServletContext();
      String savePath = context.getRealPath("/carrot");
      	try {	
             String fileName = cvrimg.getOriginalFilename();
             cvrimg.transferTo(new File(savePath + "/" + fileName));
             String cType = cvrimg.getContentType();
             String pName = cvrimg.getName();
             Resource res = cvrimg.getResource();
             long fSize = cvrimg.getSize();
             
	     cv.setCvrimg(fileName);
             cv.setImgsize(fSize/1024);
             cv.setImgtype(cType);
             
	     boolean empty = cvrimg.isEmpty();   
//--------------------------------conimg1-------------------------------------------------------------   
             for(int i=0;i<contimg1.length;i++) {
	         if (contimg1[i].getSize() == 0) continue;
	             CarrotAttach ca = new CarrotAttach();
            	     
		     String contentName1 = contimg1[i].getOriginalFilename();
            	     contimg1[i].transferTo(new File(savePath + "/" + contentName1));
                     String conType1 = contimg1[i].getContentType();
                     String conName1 = contimg1[i].getName();
                     Resource con1 = contimg1[i].getResource();
                     long conSize1 = contimg1[i].getSize();
                     boolean cont1 = contimg1[i].isEmpty();
                  
                     ca.setContimg1(contentName1);
                     ca.setContsize1(conSize1/1024);
                     ca.setConttype1(conType1);
    
		     filelist.add(ca);
                  }  
	         cv.setClist(filelist);
	         boolean added = dao.addcarrotAll(cv);
	         map.put("added", added); 
            } catch (Exception e) {
                e.printStackTrace();
            }
           return map;
   }
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 메인 사진은 1개만 필요하지만 상세설명이 필요한 하자가 여러개가 있을시 총 다수개의 사진을 받을수 있게 이용자에게 배열로 사진을 받아옵니다.</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">Addfrom.html</div>
```html
function add(){
   var file1 = document.getElementById("file1").value;
   var title = document.getElementById("title").value;
   var price  = document.getElementById("price").value;
   var content = document.getElementById("content").value;
   var file2  = document.getElementById("file2").value;


     if (title == "") {
       alert("'글 제목' 입력은 필수입니다");
       return false;
     } else if (price == "") {
       alert("'가격' 입력은 필수입니다");
       return false;
     }else if (content == "") {
      alert("'글쓰기' 입력은 필수입니다");
       return false;
     } else if (file2 == "") {
       alert("'첫번째사진' 은 필수입니다");
       return false;
       } else if (file1 == "") {
      alert("'메인사진' 은 필수입니다");
      return false;
     } 
   
   if (!confirm("글을 모두 작성하셨나요?")) {
       return false;
   }
   if (!confirm("판매 사진과 물품이 다를시 형사처벌이 될 수 있습니다. 상태를 모두 확인하셨나요?")) {
       return false;
   }
   $('#btnUpload').prop('disabled', true);
   var form = $('#addform')[0];
    var formData = new FormData(form);
   $.ajax({
      url:'/carrot/add',
       data: formData,
       enctype: 'multipart/form-data',
         method: 'post',
         cache: false,
         dataType: 'json',
         processData: false,
         contentType: false,
         timeout: 3600,
      success:function(res) {
         alert(res.added ? '저장 성공':' 저장 실패');
         location.href = "/carrot/list/page/1"

      },
      error:function(xhr,status,err) {
         alert('에러:' + err);
          $('#btnUpload').prop('disabled', false);
      }
   });
   return false;
}

function preview(evt) {
    var reader = new FileReader();

    reader.onload = function (event) {
        var thumbnailContainer = document.getElementById("thumbnail_view");
        thumbnailContainer.innerHTML = "";

        var img = document.createElement("img");
        img.src = event.target.result;
        thumbnailContainer.appendChild(img);
    };

    reader.readAsDataURL(evt.target.files[0]);
}
 ```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 메인사진과 물품 특징사진 1장은 필수로 입력해야합니다.
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
2. 메인사진과 물품 특징사진을 올릴시 function preview 함수로 인해 올려진 사진이 화면에 출력됩니다.<br>
<img src="/assets/images/add1.png" width="300px" height="800px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>
---------------------------------------------------------------------------
- 중고물품 목록

<div style="font-size : 17px; color:blue; margin-bottom: 7px">FairyMapper.xml</div>
```html
<!-- 목록을 불러옵니다 -->
<select id="getcarrot" resultType="map">
SELECT * FROM carrot
</select>
<!-- 제목을 이용한 검색한 객체만 가져옵니다 -->
<select id="search" parameterType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO"
	resultType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO">
	SELECT * FROM carrot
	<if test="title != null">
	WHERE title LIKE CONCAT('%', #{title}, '%')
	</if>	
</select>
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 제목에 일부분만 입력해도 해당 단어가 있는 모든 객체를 불러옵니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotDAO와CarrotController</div>
```java
@Component("carrotdao")
public class CarrotDAO {
	
	@Autowired
	@Qualifier("carrotmapper")
	CarrotMapper carrotmapper;
	
	public PageInfo<Map> getallcarrot(int pageNum) {
		PageHelper.startPage(pageNum,10);
		PageInfo<Map> pageInfo = new PageInfo<> (carrotmapper.getcarrot());
		
		return pageInfo;
	}
	
	public PageInfo<Map> search(String category,String keyword, int pageNum)
	{
		PageHelper.startPage(pageNum,10);
		CarrotVO cv = new CarrotVO();
		   if(category.equals("title")) {cv.setTitle(keyword);}
		PageInfo<Map> pageInfo = new PageInfo<>(carrotmapper.search(cv));
		return pageInfo;
		
	}
}

@Controller
@RequestMapping("/carrot")
@SessionAttributes("userid")
public class CarrotController {

   @Autowired
   @Qualifier("carrotdao")
    CarrotDAO dao;

   @GetMapping("/list/page/{pn}")
    public String getlist(@PathVariable int pn,
	         	  @RequestParam(value="category", required=false) String category,
	        	  @RequestParam(value="keyword", required=false) String keyword, Model model,
	        	  @SessionAttribute(name = "userid", required = false)String userid)
	   {
	      PageInfo<Map> pageInfo = null;
	      List<CarrotAttach> list = new ArrayList<>();
	        if(category !=null)
	        {
	          pageInfo = dao.search(category, keyword, pn);
	          model.addAttribute("category" , category);
	          model.addAttribute("keyword", keyword);
	        }else {
	           pageInfo = dao.getallcarrot(pn);
	        }
	      model.addAttribute("pageInfo",pageInfo);

	      return "thymeleaf/carrot/list";
	   }
	
	@PostMapping("/search")
	public String search(@RequestParam("category") String category,
			     @RequestParam("keyword") String keyword,Model model)
	    {
		PageInfo<Map> pageInfo = new PageInfo<>();
		
		if(category.equals("title")) {
		    pageInfo = dao.search(category, keyword,1);
		}
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("category", category);
		model.addAttribute("keyword", keyword);
		
		return "thymeleaf/carrot/list";
		
	    }
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 여러개의 목록은 PageInfo로 처리했고 1개의 html에서 출력됩니다.</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">List.html</div>
```html
<th:block th:switch="${param.category}">
 <th:block th:case="'title'">
    <h1>검색 목록</h1>
    <form id="day" action="/carrot/search" method="post">
     <table>
      <thead>
       <tr>
        <th>이미지</th>
        <th>내용</th>
        <th>날짜</th>
        <th>HIT</th>
        <th>판매상태</th>
       </tr>
      </thead>

      <tbody>
       <tr th:each="item : ${pageInfo.list}">
        <td><img th:src="@{'/carrot/' + ${item.cvrimg}}" style="max-width: 100px; max-height: 100px;"></td>
        <td><a th:href="@{/carrot/detail/{cnum}(cnum=${item.cnum})}" th:text="${item.title}"></a></td>
        <td><span th:text="${#dates.format(item['cdata'], 'yyyy-MM-dd')}"></span></td>
        <td th:text="${item.hitnum}"></td>
        <td th:text="${item.state}" style="text-decoration:line-through"></td>
       </tr>
      </tbody>
     </table>
    </form>
 </th:block>
  
<!-- 다른 경우(카테고리 없음) -->
 <th:block th:case="*">
     <h1>상품목록</h1><hr>
     <table>
      <div id ="vib"><a href="/carrot/add">추가</a></div>
       <thead>
        <tr>
         <th>이미지</th>
         <th>내용</th>
         <th>날짜</th>
         <th>HIT</th>
         <th>판매상태</th>
        </tr>
       </thead>
 
       <tbody>
 	<tr th:each="item : ${pageInfo.list}">
          <span th:if="${item.state != '판매완료'}">
 	    <td><img th:src="@{'/carrot/' + ${item.cvrimg}}" style="max-width: 100px; max-height: 100px;"></td>
 	    <td><a th:href="@{/carrot/detail/{cnum}(cnum=${item.cnum})}" th:text="${item.title}"></a>
 	    <span th:if="${item.state == '판매완료'}" th:text="${item.title}"  style="text-decoration:line-through"></span></td>
 	    <td><span th:text="${#dates.format(item['cdata'], 'yyyy-MM-dd')}"></span></td>
 	    <td th:text="${item.hitnum}"></td>
	    <td th:text="${item.state}"></td>
	  </span>
	</tr>
      </tbody>
     </table>
    </th:block>
</th:block>

 ```
<div style = "font-size : 15px; margin-bottom: 7px">
1. switch와 case를 이용해서 GET,POST방식을 한개의 html로 호출해 봤습니다.
</div>
<div style = "font-size : 15px; margin-bottom: 7px">
2. 판매가 완료된 상품에경우 목록에서 보이지 않게 처리 했습니다.
</div>
<div style = "font-size : 15px; margin-bottom: 7px">
3. 제목을 누를경우 해당 물건의 상세정보창으로 이동합니다.<br>
</div>

-----------------------------------------------------------------------------

- DETAIL 페이지
<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotMapper.xml</div>
```html

<!-- 목록에서 선택한 객체의 정보를 불러옴 -->
<select id="getcarrottoattach" resultType="java.util.Map" parameterType="Integer">
  SELECT b.*,a.num,a.contimg1
  FROM carrot b
  LEFT OUTER JOIN carrotattach a ON b.cnum = a.cnum
  where b.cnum=#{cnum}
</select>

<!-- 목록에서 제목을 클릭할시 조회수 1추가됨 -->
<update id="gethit" parameterType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO">
  UPDATE carrot SET hitnum = hitnum + 1 WHERE cnum = #{cnum}
</update>

<!-- 찜하기 기능 -->
<insert id="interest" parameterType="com.ezen.spring.board.teampro.carrotmarket.Interest">
  INSERT INTO interest values(NULL,#{cnum},#{userid},#{mainimg},#{title},#{price},#{state})
</insert>

<!-- 구매하기 기능 -->
<select id="get_to_buy" resultType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO" parameterType="Integer">
  SELECT * FROM carrot WHERE cnum = #{cnum}
</select>
 ```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 디테일창에서 찜하기와 구매하기가 가능합니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotController</div>
```java

@Controller
@RequestMapping("/carrot")
@SessionAttributes("userid")
public class CarrotController {

   @Autowired
   @Qualifier("carrotdao")
    CarrotDAO dao;

   @GetMapping("/detail/{cnum}")
	public String getcarrot(@PathVariable int cnum, Model model,CarrotVO cv,
				@SessionAttribute(name = "userid", required = false)String userid) {

	    List<Map> clist = dao.getcarrotandattach(cnum);
	    
	    dao.hitup(cv);
	    
  	    List<Map> comlist = new ArrayList<>();
	    comlist = dao.comment(cnum);
		
	    List<Map> comtolist = new ArrayList<>();
	    comtolist = dao.commenttocom();
	   
	    model.addAttribute("comto", comtolist);
	    model.addAttribute("com", comlist);
	    model.addAttribute("detail", clist);
	    return "thymeleaf/carrot/detail";
	}
   //찜하기
   @PostMapping("/interest")
   @ResponseBody
    public Map<String,Boolean> interestadd(Interest in){
	Map<String,Boolean> map = new HashMap<>();
	
	map.put("inter", dao.interests(in));
	return map;
	}
   //구매하기
   @PostMapping("/carrotbuy")
   @ResponseBody
    public String buyCart(@RequestParam int cnum, HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    
	    List<CarrotVO> existingList = (List<CarrotVO>) session.getAttribute("cartbuy");
	    if (existingList == null) {
	    	existingList = new ArrayList<>();
	    }
	    for(Iterator<CarrotVO> iterator = existingList.iterator(); iterator.hasNext();) {
	    	CarrotVO cv = iterator.next();
                if (cv.getTitle().equals(title)) {
                    iterator.remove();
                    break;
            	    }
	        } 
	    existingList.add(dao.session_carrot(cnum));
	    session.setAttribute("cartbuy", existingList);
	    
	    return "cart/buycart"; // 해당 뷰로 이동
	}
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. deteil페이지에서는 찜과 구매하기가 가능한데 구매시 객체의 정보는 세션에 담깁니다.</div>
<div style = "font-size : 15px; margin-bottom: 7px">
2. 이미 세션에 담긴객체면 중복되지 않게 기존객체는 삭제됩니다.</div>
<div style = "font-size : 15px; color:red; margin-bottom: 15px">
3. model에 담겨진comto와 com은 아래의 댓글기능에서 설명하겠습니다.</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">detail.html</div>
```html

<!-- 출력물은 중요한게 없어서 생략하겠습니다. -->
<button type="button" onclick="javascript:interest();">찜하기</button>
<button type="button" onclick="javascript:buycarrot();">구매하기</button>

function interest(){
  if (!confirm('찜 할까요?')) return;
	
  var obj = {};
      obj.cnum = '[[${detail[0].cnum}]]';
      obj.userid = $('#userid').val();
      obj.mainimg = '[[${detail[0].cvrimg}]]';
      obj.title = '[[${detail[0].title}]]';
      obj.price = '[[${detail[0].price}]]';
      obj.state = '판매중'
    
  $.ajax({
      url: '/carrot/interest',
      method: 'post',
      data: obj,
      cache: false,
      dataType: 'json',
      success: function(res) {
        if (res.inter) {
          alert('찜 되었습니다.');
          location.reload();
        }
      },
      error: function(xhr, status, err) {
        alert(status + "/" + err);
      }
    });
  }

function buycarrot() {
    if (!confirm("선택한 항목을 주문하시겠습니까?"))return;

    var item = {
        cnum: '[[${detail[0].cnum}]]',
        title: '[[${detail[0].title}]]',
    };

    $.ajax({
        url: '/carrot/carrotbuy',
        method: 'get',
        data: item, 
        cache: false,
        success: function (res) {
            location.href = "/fairy/cart/buy";
        },
        error: function(xhr, status, err) {
            alert(status + "/" + err);
        }
    });
}
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 찜 기능이 정상적으로 작동합니다.<br>
<img src="/assets/images/intre.png" width="800px" height="300px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
2. 구매버튼이 뜨면 알림창이뜨고 바로 구매창으로 넘어갑니다.<br>
<img src="/assets/images/buy2.png" width="800px" height="300px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>

--------------------------------------------------------------------

- 댓글기능

<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotcommentVO</div>
```java

public class Carrotcomment {

	private int num;
	private int carnum;
	private String userid;
	private String comment;
	private String cardata;
}
```
<div style = "font-size : 15px; margin-bottom: 7px">
위에는 댓글 VO이고 아래는 답글 Vo입니다.
</div>
```java

public class Carrotcomment1 {

	private int num;
	private int dcarnum;
	private String duserid;
	private String dcomment;
	private String dcardata;
}
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 댓글과 답글이 필요하기에 두개의 VO를 만들었습니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotMapper.xml</div>
```html
<!-- 구매자가 댓글을 답니다  -->
<insert id ="comtadd" parameterType="com.ezen.spring.board.teampro.carrotmarket.Carrotcomment">
INSERT INTO carrotcomment (carnum,userid,comment,cardata) values(#{carnum},#{userid},#{comment}, CURRENT_TIMESTAMP)
</insert>

<select id="getcomment" resultType="map">
SELECT * FROM carrotcomment WHERE carnum = #{carnum} 
</select>
<!-- 판매자가 답글을 답니다  -->
<insert id ="comtocomadd" parameterType="com.ezen.spring.board.teampro.carrotmarket.Carrotcomment1">
INSERT INTO carrotcomment1 (dcarnum,duserid,dcomment,dcardata) values(#{dcarnum},#{duserid},#{dcomment}, CURRENT_TIMESTAMP)
</insert>

<select id="getcommenttocom" resultType="map">
SELECT * FROM carrotcomment1
</select>

```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 판매자와 구매자의 댓글 추가와 출력부분 모두 한번에 처리합니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotController</div>
```java

  @PostMapping("/comment")
  @ResponseBody
   public Map<String,Boolean> comadd(Carrotcomment cc){
	  Map<String,Boolean> map = new HashMap<>();
	 
	  map.put("com", dao.comadd(cc));
	  
	  return map;
	}
	
  @PostMapping("/commentocom")
  @ResponseBody
   public Map<String,Boolean> com2comadd(Carrotcomment1 cc){
	  Map<String,Boolean> map = new HashMap<>();
	
	  map.put("comto", dao.comtoadd(cc));
	
	  return map;
	}

```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 댓글과 답글을 달수있는 로직입니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">detail.html</div>
```html
function comment(){
   if (!confirm('댓글을 등록할까요?')) return;
	
   var obj = {};
       obj.carnum = '[[${detail[0].cnum}]]';
       obj.userid = $('#userid').val();
       obj.comment = $('#comment').val();

    $.ajax({
      url: '/carrot/comment',
      method: 'post',
      data: obj,
      cache: false,
      dataType: 'json',
      success: function(res) {
        if (res.com) {
          alert('등록 되었습니다.');
          location.reload();
        }
      },
      error: function(xhr, status, err) {
        alert(status + "/" + err);
      }
    });
  }
  
function to_comment() {
    if (!confirm('답글을 등록할까요?')) return;

    var obj = {};
        obj.duserid = $('#userid').val();
        obj.dcomment = $('#dcomment').val();
        obj.dcarnum = $('#num').val();

 

    $.ajax({
        url: '/carrot/commentocom',
        method: 'post',
        data: obj,
        cache: false,
        dataType: 'json',
        success: function(res) {
             if (res.comto) {
                 alert('등록 되었습니다.');
                 location.reload();
                }
            },
        error: function(xhr, status, err) {
              alert(status + "/" + err);
            }
        });
  
}

<table>
 <h3>댓글달기</h3>
 <table>
  <div th:each="item : ${com}">
   <tr>
    <td>
       <script>
	var titleFromServer = "[[${item.num}]]"; 
		        
	var titleElement = document.getElementById("num");
	titleElement.value = titleFromServer;
       </script>
		   		
       [[${item.userid}]]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       [[${item.comment}]]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       <span th:text="${#dates.format(item['cardata'], 'yyyy-MM-dd')}"></span>      
   </td>
  </tr>
    
 <div th:each="itemto : ${comto}">   
   <tr th:if="${item.num == itemto.dcarnum}">
     <td>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp&nbsp;
	 [[${itemto.duserid}]]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         [[${itemto.dcomment}]]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         <span th:text="${#dates.format(itemto['dcardata'], 'yyyy-MM-dd')}"></span>
     </td>
   </tr>
</div>

<tr>
  <td>
    <input type="text" id ="dcomment" name = "dcomment" style="width: 500px; padding: 3px;">
    <button type="button" th:attr="onclick='to_comment(\'' + ${item.num} + '\')'">등록</button>
  </td>
</tr>
</div>

</table>
  <from id ="addcom">
	<input type="hidden" id ="userid" name = "userid">
	<script>
        var userFromServer = "[[${userid}]]"; 
        
        var userElement = document.getElementById("userid");
        userElement.value = userFromServer;
   	</script>
	<input type="text" id ="comment" name = "comment" style="width: 500px; padding: 3px;">
	<button type="button" onclick="javascript:comment()">등록</button>
  </from>
</table>
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 댓글과 답글을 테스트 해보았습니다.<br>
<img src="/assets/images/comment.png" width="400px" height="300px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>
<div style = "font-size : 15px; margin-bottom: 7px">
2. 위의 detail중 controller에서 model에 담긴 comto와 com은 여기서 사용됩니다.<br>
<img src="/assets/images/comto.png" width="400px" height="300px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>

-----------------------------------------------------------------------------

- 수정 기능

<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotMapper.xml</div>
```html

<!-- 수정SQL 로직 입니다 -->
<update id="updatecarrot" parameterType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO">
    UPDATE test.carrot
    SET
        <if test="cvrimg != null">
            cvrimg = #{cvrimg},
        </if>
        price = #{price},
        title = #{title},
        content = #{content}
    WHERE cnum = #{cnum}
</update>

<!-- 특징사진 삭제 로직 입니다 -->
<delete id="delattach" parameterType="Integer">
DELETE FROM test.carrotattach WHERE num = #{num}
</delete>
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 사진변경시를 위한 삭제로직 필요합니다.</div>


<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotDAO와CarrotController</div>
```java
@Component("carrotdao")
public class CarrotDAO {
	
	@Autowired
	@Qualifier("carrotmapper")
	CarrotMapper carrotmapper;
	//수정 로직
	public boolean updateCarrot(CarrotVO cv,int cnum)
	{
		boolean bSaved = carrotmapper.updatecarrot(cv)>0;
		
	
		List<CarrotAttach> list = cv.getClist();
		if (list != null && !list.isEmpty()) {
			 for (int i = 0; i < list.size(); i++) {
		         list.get(i).setCnum(cnum);
		        }
		    	boolean att = carrotmapper.addcarrotfile(cv.getClist())> 0;
			        if (!bSaved) {
			            return false;
			        }
		    }
		    return bSaved;
		
	}
	//사진 삭제로직
	public boolean deleteatt(int num) {
		return carrotmapper.delattach(num)>0;
	}
}

@Controller
@RequestMapping("/carrot")
@SessionAttributes("userid")
public class CarrotController {

   @Autowired
   @Qualifier("carrotdao")
    CarrotDAO dao;

   @PostMapping("/update")
    @ResponseBody
       public Map<String, Object> updatecarrot(@SessionAttribute(name = "userid", required = false)String userid,
    		   									@RequestParam(value ="file1", required=false) MultipartFile cvrimg,
    		   									@RequestParam(value ="file2", required=false) MultipartFile[] contimg1,
    		   									CarrotVO cv,HttpServletRequest request,@RequestParam int cnum) 
      {
		

      Map<String, Object> map = new HashMap<>();
      ServletContext context = request.getServletContext();
      String savePath = context.getRealPath("/carrot");
      List<CarrotAttach> filelist = new ArrayList<>();
      
           try {	
        	 if(cvrimg != null && !cvrimg.isEmpty()) 
		{
                 String fileName = cvrimg.getOriginalFilename();

                 cvrimg.transferTo(new File(savePath + "/" + fileName));

                 String cType = cvrimg.getContentType();
                 String pName = cvrimg.getName();
                 Resource res = cvrimg.getResource();
                 long fSize = cvrimg.getSize();

                 cv.setCvrimg(fileName);
                 cv.setImgsize(fSize/1024);
                 cv.setImgtype(cType);
                 boolean empty = cvrimg.isEmpty();
//--------------------------------conimg1-------------------------------------------------------------   
//--------------------------------conimg1-------------------------------------------------------------   
               for(int i=0;i<contimg1.length;i++) {
	                if (contimg1[i].getSize() == 0) continue;
	               CarrotAttach ca = new CarrotAttach();

	               String contentName1 = contimg1[i].getOriginalFilename();

	               contimg1[i].transferTo(new File(savePath + "/" + contentName1));

	               String conType1 = contimg1[i].getContentType();
	               String conName1 = contimg1[i].getName();
	               Resource con1 = contimg1[i].getResource();
	               long conSize1 = contimg1[i].getSize();
	               boolean cont1 = contimg1[i].isEmpty();
	               
	               ca.setContimg1(contentName1);
	               ca.setContsize1(conSize1/1024);
	               ca.setConttype1(conType1);
 
	               filelist.add(ca);
                       }  
	       cv.setClist(filelist);

	       boolean update = dao.updateCarrot(cv,cnum);
	       map.put("updated", update);
               }else if(contimg1 != null && !contimg1[0].isEmpty()) {
        		   for(int i=0;i<contimg1.length;i++) {

      	               		CarrotAttach ca = new CarrotAttach();

      	                        String contentName1 = contimg1[i].getOriginalFilename();

      	        		contimg1[i].transferTo(new File(savePath + "/" + contentName1));

      	            		String conType1 = contimg1[i].getContentType();
      	               		String conName1 = contimg1[i].getName();
      	               		Resource con1 = contimg1[i].getResource();
      	               		long conSize1 = contimg1[i].getSize();
      	               		boolean cont1 = contimg1[i].isEmpty();
      	               
      	               		ca.setContimg1(contentName1);
      	               		ca.setContsize1(conSize1/1024);
      	               		ca.setConttype1(conType1);
       
      	               		filelist.add(ca);
                     		}  
      	        	  cv.setClist(filelist);

      	        	  boolean update = dao.updateCarrot(cv,cnum);
      	        	  map.put("updated", update);
        	   }else {
   	        	  boolean update = dao.updateCarrot(cv,cnum);
   	        	  map.put("updated", update);
        	   }
            } catch (Exception e) {
                e.printStackTrace();
            }
           return map;
   }
	
	@PostMapping("/del")//첨부물 삭제로직
	@ResponseBody
	public Map<String,Boolean> delatt(@RequestParam int num){
		Map<String,Boolean> map = new HashMap<>();
		map.put("delete", dao.deleteatt(num));
		return map;
	}
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 추가 로직과 유사하지만 추가된 첨부파일이 없다면  중간에 conimg1쪽로직은 실행되지 않습니다.</div>
<div style = "font-size : 15px; margin-bottom: 15px">
2. 반대로 추가가 됬다면.사진까지 추가되는 로직이 실행됩니다.</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">Updatefrom.html</div>

```html
<!--대체로 추가기능과 유사하기 때문에 짧게 특징만 보여드리겠습니다 -->
<div th:each="img : ${up}">
    <div th:if="${up[0].contimg1.length() > 0}">
         img th:src="@{'/carrot/' + ${img.contimg1}}" style="max-width: 100px; max-height: 100px;">
         <a th:href="'javascript:attremove(' + ${img.num} + ');'" title="첨부파일 삭제">[X]<br></a>
    </div>
</div>

<!-- 서버에서 받아온 list의 size만큼 동적으로 사진업로드 input이 생성됩니다. -->
<div th:switch="${up.size()}"> 
    <th:block th:case="0">
	
    <label for="file2">이미지1</label>
    <input type="file" id="file2" name="file2" onchange="preview1(event);">
    <div id="thumbnail_view1">
        <span>Preview Image</span>
    </div>
        <label for="file2">이미지2</label>
        <input type="file" id="file3" name="file2" onchange="preview2(event);">
        <div id="thumbnail_view2">
            <span>Preview Image</span>
        </div>
        
        <label for="file2">이미지3</label>
        <input type="file" id="file4" name="file2" onchange="preview3(event);">
        <div id="thumbnail_view3">
            <span>Preview Image</span>
        </div>
        
        <label for="file2">이미지4</label>
        <input type="file" id="file5" name="file2" onchange="preview4(event);">
        <div id="thumbnail_view4">
            <span>Preview Image</span>
        </div>
    </th:block>
<!-- 사진 업데이트시 사진이 1개만 있다면 -->     
    <th:block th:case="1">
        <label for="file2">이미지2</label>
        <input type="file" id="file3" name="file2" onchange="preview2(event);">
        <div id="thumbnail_view2">
            <span>Preview Image</span>
        </div>
        
        <label for="file2">이미지3</label>
        <input type="file" id="file4" name="file2" onchange="preview3(event);">
        <div id="thumbnail_view3">
            <span>Preview Image</span>
        </div>
        
        <label for="file2">이미지4</label>
        <input type="file" id="file5" name="file2" onchange="preview4(event);">
        <div id="thumbnail_view4">
            <span>Preview Image</span>
        </div>
    </th:block>

<!-- 사진 업데이트시 사진이 2개만 있다면 -->     
    <th:block th:case="2">
        <label for="file2">이미지3</label>
        <input type="file" id="file4" name="file2" onchange="preview3(event);">
        <div id="thumbnail_view3">
            <span>Preview Image</span>
        </div>
        
        <label for="file2">이미지4</label>
        <input type="file" id="file5" name="file2" onchange="preview4(event);">
        <div id="thumbnail_view4">
            <span>Preview Image</span>
        </div>
    </th:block>

<!--사진 업데이트시 사진이 3개만 있다면 -->      
    <th:block th:case="*">
        <label for="file2">이미지4</label>
        <input type="file" id="file5" name="file2" onchange="preview4(event);">
        <div id="thumbnail_view4">
            <span>Preview Image</span>
        </div>
    </th:block>
</div>
 ```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 만약 사진이 3장이 이미 존재한다면 동적으로 1개의 사진 추가칸이 뜹니다.
<img src="/assets/images/cup1.png" width="300px" height="800px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>
-----------------------------------------------------------------------------

- 삭제기능

<div style="font-size : 17px; color:blue; margin-bottom: 7px">FairyMapper.xml</div>
```html

<!-- 로그인한 이용자가 올린 중고책 목록을 불러옵니다 -->
<select id="get_to_my" resultType="com.ezen.spring.board.teampro.carrotmarket.CarrotVO" parameterType="String">
SELECT * FROM carrot WHERE userid = #{userid}
</select>

<!-- 삭제SQL문 입니다 -->
<delete id="delcarrot" parameterType="Integer">
DELETE FROM test.carrot WHERE cnum = #{cnum}
</delete>

<delete id="delcarrot_att" parameterType="Integer">
DELETE FROM test.carrotattach WHERE cnum = #{cnum}
</delete>
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 우선 이용자의 중고책 목록을 불러옵니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">CarrotDAO와CarrotController</div>
```java

@Component("carrotdao")
public class CarrotDAO {
	
	@Autowired
	@Qualifier("carrotmapper")
	CarrotMapper carrotmapper;

	public List<Map> mypage(String userid){
	      List<Map> page = carrotmapper.get_to_my(userid);
	      return page;
	   }

	public boolean All_del(int cnum) {
		boolean carrotatt = carrotmapper.delcarrot_att(cnum)>0;
		boolean carrot = carrotmapper.delcarrot(cnum)>0;
		return carrotatt&&carrot;
	}
}

@Controller
@RequestMapping("/carrot")
@SessionAttributes("userid")
public class CarrotController {

   @Autowired
   @Qualifier("carrotdao")
    CarrotDAO dao;

   //이용자의 중고책 목록을 불러옵니다.
	@GetMapping("/mypage")
	public String mypage(@SessionAttribute(name = "userid", required = false)String userid,Model model) {
		List<Map> mylist = dao.mypage(userid);
		model.addAttribute("my",mylist);
		return "thymeleaf/carrot/mylist";
	}
	
	//원하는 물품을 삭제하는 로직입니다.
	@PostMapping("/Alldel")
	@ResponseBody
	public Map<String,Boolean> interestadd(int cnum){
		Map<String,Boolean> map = new HashMap<>();
		
		map.put("remove", dao.All_del(cnum));
		return map;
	}
}
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 목록을 불러온후 웹화면에서 삭제가 가능합니다.</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">List.html</div>
```html
<!-- 해당 기능에 대한 코드만 올리겟습니다. -->
function del(cnum){
	if (!confirm("물품을 삭제겠습니까?")) {
	    return false;
	}
	var obj = {};
	obj.cnum = cnum;

	$.ajax({
	    url: '/carrot/Alldel',
	    data:obj,
	    method: 'post',
	    cache: false,
	    dataType: 'json',  
	    success: function(res) {
	    	var message = res.remove ? "삭제 성공" : "삭제 실패";
	        alert(message);
	        	location.reload();
	        
	    },
	    error: function(err) {
	        alert('에러: ' + err);
	    }
	});
}

<button type="button" th:attr="onclick='del(\'' + ${item.cnum} + '\')'">삭제</button>

 ```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 모든 삭제로직은 어려울 것 없이 SQL문과 필요한 컬럼만 서버로 넘겨주면 쉽게 작동합니다.<br> 
<img src="/assets/images/cdel.png" width="800px" height="200px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>

<div style = "font-size : 20px; color:red; margin-bottom: 15px">
END
</div>
-----------------------------------------------------------------------------
<h3>개발 환경</h3>
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">언어</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Java(JDK17), HTML/CSS/JSP, Thymeleaf, JavaScript</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">서버</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Apache Tomcat10.1</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">프레임워크</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Spring Boot,MyBatis</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">DB</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">MySQL8.0</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">IDE</span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Eclipse</span></div>

<div style ="margin-bottom: 10px; font-size : 15px; "><span style="font-weight: bold;">API, 라이브러리</span>&nbsp;&nbsp;<span style="color:gray;">Jquery, Maven</span></div>
