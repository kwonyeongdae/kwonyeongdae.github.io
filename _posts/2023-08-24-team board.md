---
title:  "첫번째 팀 프로젝트 - 문의사항 페이지"
excerpt: "아동용 책판매 쇼핑몰 - 문의사항"

categories:
  - team preject
tags:
  - java
  - spring
  - jsp
  - MySQL
last_modified_at: 2023-08-24 19:23PM
---
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발 기간</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 2023-07 ~ 2023-09(3개월)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">플랫폼</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Web</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발인원</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">3명(팀장)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">담당 역할</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 문의사항 페이지 개발(기여도 100%) 프론드엔드-jsp(기여도 100%)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold; ">전체 코드&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><a href="https://github.com/kwonyeongdae/portfolio/tree/main/Board" style="text-decoration: none;"> github 보러가기</a></div>
----------------------------------------------------
<h3>프로젝트</h3>

- 추가기능
<div style="font-size : 17px; color:bule; margin-bottom: 7px">BoardVO</div>
```java
public class BoardVO {
	private int bnum;
    	private String bname;
    	private String title;
    	private String content;
   	private java.util.Date date = new Date();
  	private int hitnum;
   	private List<FileVO> flist;
   	private List<Comment> clist;
    	private String pass;
    	private String name;
	}
```
<div style = "font-size : 15px; margin-bottom: 7px">
게시판을 만들기 위한 컬럼은 총10개로 준비 했습니다.</div>
<div style = "font-size : 15px; margin-bottom: 15px">
List-FileVO는 문의사항에 첨부물이 올라갔을 경우이고 List-Comment는 문의사항에 관리자가 답글을 달기위해 만들어 두었습니다.
</div>


<div style="font-size : 17px; color:blue; margin-bottom: 7px">BoardMapper.XML</div>
```html
<insert id="add" parameterType="com.ezen.spring.board.teampro.board2.Boardvo">
   <selectKey keyProperty="bnum" resultType="int" order="AFTER">
   	SELECT LAST_INSERT_ID()
   </selectKey>
   INSERT INTO board VALUES(NULL,#{bname},#{title},#{content},CURDATE(),#{hitnum},#{pass})
   <!--Board등록시 DB에 데이터를 추가하는 로직 -->
</insert>
<insert id="addfile" parameterType="com.ezen.spring.board.teampro.board2.FileVO">
    INSERT INTO attach (num, pnum, fname, fsize, contenttype) VALUES
    <foreach collection="list" item="item" separator=", ">
       ( NULL, #{item.pnum}, #{item.fname}, #{item.fsize}, #{item.contentType} )
    </foreach>
    <!--Board등록시 첨부물이 있을경우 DB에 첨부파일 데이터를 추가하는 로직 -->
</insert>
```
<div style = "font-size : 15px; margin-bottom: 7px">
문의사항 등록시 사용자가 사진파일을 보낼수 있다는 가정하에 첨부물도 추가할수 있게 만들어서 총 2개의 SQL추가 로직이 필요합니다.
</div>
<div style = "font-size : 15px; margin-bottom: 7px">
SELECT LAST_INSERT_ID는 데이터베이스에서 마지막으로 삽입된 레코드의 자동 생성된 키 값을 가져오는 SQL 문입니다.
</div>
<div style = "font-size : 15px; margin-bottom: 7px">
selectKey keyProperty="bnum" resultType="int" order=AFTER 이 부분은 데이터베이스에서 생성된 키 값을 반환하기 위해 사용하는 -selectKey- 요소입니다. 
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
keyProperty는 생성된 키 값을 어떤 프로퍼티에 저장할 지를 지정하며 resultType은 반환되는 키 값의 데이터 타입을 나타냅니다. order는 키 값이 생성된 후에 가져올 것인지 생성 전에 가져올 것인지를 지정합니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">BoardMapper와 BoardDAO</div>
```java
//Board Mapper
@Component("boardmapper")
@Mapper
public interface BoardMapper {
	
	public int add(Boardvo vo);

	public int addfile(List<FileVO> list);

//Board DAO
public class BoardDao {
	@Autowired
	@Qualifier("boardmapper")
	private BoardMapper bm;
	
	public boolean add(Boardvo vo)
	{
		boolean bSaved = bm.add(vo)>0;
		
		int pnum = vo.getBnum();
		List<FileVO> list = vo.getFlist();
		if (list != null && !list.isEmpty()) {
		    for (int i = 0; i < list.size(); i++) {
		         list.get(i).setPnum(pnum);
		        }
		    	boolean att = bm.addfile(vo.getFlist()) > 0;
			        if (!bSaved) {
			            return false;
			        }
		    }
		    return bSaved;
	}

```
<div style = "font-size : 15px; margin-bottom: 7px">
문의사항 등록시 첨부물이 없을경우를 대비해서 첨부물이 없을 경우엔 글만 등록 할 수 있게 만들었습니다.
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
첨부물이 있다면 list에 저장되지만 만약 list가 있다면 첨부물도 저장되겠지만 그게 아니라면 글만 저장 됩니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">Board Controller</div>
```java
@Controller
@RequestMapping("/board")
@SessionAttributes("userid")
public class BoardController {
	@Autowired
	@Qualifier("boarddao")
	BoardDao bd;

	@PostMapping("/board/add")
	@ResponseBody
	   public Map<String, Object> add(@SessionAttribute(name = "userid", required = false)String userid, Boardvo bo,
					  @RequestParam("files") MultipartFile[] mfiles,HttpServletRequest request) {
	      
	      Map<String, Object> map = new HashMap<>();
	      ServletContext context = request.getServletContext();
	       String savePath = context.getRealPath("/files");
	       List<FileVO> fileList = new ArrayList<>();
	          try {
	             for(int i=0;i<mfiles.length;i++) {
	                  if (mfiles[i].getSize() == 0) continue;

	                  String fileName = mfiles[i].getOriginalFilename();
	                  mfiles[i].transferTo(new File(savePath + "/" + fileName));
	                  String cType = mfiles[i].getContentType();
	                  String pName = mfiles[i].getName();
	                  Resource res = mfiles[i].getResource();
	                  long fSize = mfiles[i].getSize();
	                  boolean empty = mfiles[i].isEmpty();
	              
	                  FileVO vo = new FileVO();
	                  vo.setFname(fileName);
	                  vo.setFsize(fSize/1024);
	                  vo.setContentType(cType);
	                  
	                  fileList.add(vo);
	              }
	             bo.setFlist(fileList);
	              boolean added = bd.add(bo);
	             // System.out.println(added);
	              if (userid != null) {
	                 String id = userid;
	                 map.put("added", added);
	              }else {
	                 map.put("added", false);
	              }
	              return map;
	          } catch (Exception e) {
	              e.printStackTrace();
	          }
	          map.put("added", false);
	      return map;
	   }
```
<div style="font-size : 17px; margin-bottom: 7px">
첨부물은 2개만 받기로 정해서 MultipartFile[]를 배열로 받기로 햇습니다.
</div>
<div style="font-size : 17px; margin-bottom: 15px">
@SessionAttribute(name = "userid", required = false)String userid는 로그인한 유저만 글등록을 가능하게 만들었기 때문에 로직을 추가 했습니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">ADDform 페이지-jsp</div>
```html
<script src="https://code.jquery.com/jquery-3.7.0.min.js" integrity="sha256-2Pmvv0kuTBOenSvLm6bvfBSSHrUJ+3A7x6P5Ebd07/g=" crossorigin="anonymous"></script>
<script type="text/javascript">
function limitPasswordLength(input, maxLength) {
	  // 입력값의 길이를 확인하여 maxLength를 넘어서면 입력을 제한합니다.
	  if (input.value.length > maxLength) {
	    input.value = input.value.slice(0, maxLength); // 최대 길이만큼 잘라냅니다.
	  }
	}   

function added() {
	if (!confirm("추가 할까요?")) {
	    return false;
	}
    	  var title = document.getElementById("title").value;
    	  var content  = document.getElementById("content").value;

    	  if (title == "") {
    	    alert("'제목' 입력은 필수입니다");
    	    return false;
    	  } else if (content == "") {
    	    alert("'내용' 입력은 필수입니다");
    	    return false;
    	  } 
    	
        $('#btnUpload').prop('disabled', true);
        var form = $('#addform')[0];
        var formData = new FormData(form);
        $.ajax({
            url: '/board/board/add',
            data: formData,
            enctype: 'multipart/form-data',
            method: 'post',
            cache: false,
            dataType: 'json',
            processData: false,
            contentType: false,
            timeout: 3600,
            success: function (res) {
                var message = res.added ? "추가 성공" : "추가 실패";
                alert(message);
                if (res.added) {
                    location.href = "/board/board/list/page/1";
                }

            },
            error: function (err) {
                alert('에러: ' + err);
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
</script>
</head>
<body>
<h1>ADDFORM</h1>
<hr>
<div id="form-container">
    <form id="addform" enctype="multipart/form-data" onsubmit="return added();">
        <input type="hidden" id="data" name="data" value="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />">

        <fieldset>
            <legend>TEXT FIELD</legend>
	    //비밀번호는 4자리 까지만 입력하게 막아두기
            <label for="bname">비밀번호*4자리까지*</label>
            <input type="text" id="pass" name="pass" oninput="limitPasswordLength(this, 4)">
            
            <label for="bname">아이디</label>
            <input type="text" id="bname" name="bname" value="${userid}">

            <label for="title">제목</label>
            <input type="text" id="title" name="title">

            <label for="content">내용</label>
            <input type="text" id="content" name="content">
        </fieldset>

        <fieldset>
            <legend>첨부파일</legend>
            <label for="file">Upload an Image</label>
            <input type="file" id="file" name="files" onchange="preview(event);">

            <div id="thumbnail_view">
                <span>Preview Image</span>
            </div>
        </fieldset>

        
            <button type="reset">취소</button>
            <button id="btnUpload" type="submit">확인</button>
        
    </form>
</div>
</body>
```
<div style="font-size : 15px; margin-bottom: 7px">
문의사항에서 제목과 내용은 빠질수 없기 때문에 javascript로 제목이나 내용이 입력 되지 않았다면 저장이 되지 않게 만들었습니다.</div>
<img src="/assets/images/con1.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck" style ="float: left;"/>
<img src="/assets/images/ti1.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck" style ="clear: left;"/><br>

<div style="font-size : 15px; margin-bottom: 7px">
아이디는 세션을 가져오기 때문에 로그인을 한다면 자동으로 아이디를 입력하게 만들었습니다. 
</div>
<div style="font-size : 15px; margin-bottom: 7px">
비밀번호는 4자리만 입력하게 막아뒀는데 이것도 javascript로 처리했습니다.
</div>
<img src="/assets/images/add.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/><br>
<div style="font-size : 15px; margin-bottom: 7px">
add기능이 정상적으로 돌아갑니다. 사진파일을 올린다면 어떤사진인지 클라이언트가 볼수 있게 만들었습니다 텍스트 파일이라면 아무것도 뜨지 않습니다.
</div><br>

<hr>

- 목록기능

<div style="font-size : 17px; color:bule; margin-bottom: 7px">BoardMapper.XML</div>
```html
<!--목록창 -->
<select id="getList" resultType="java.util.Map">
	SELECT b.bnum,bname,title,content,date,hitnum,b.pass,
	GROUP_CONCAT(a.num) fnums,
	GROUP_CONCAT(pnum) pnums,
	GROUP_CONCAT(fname) fname,
	GROUP_CONCAT(fsize) fsizes,
	GROUP_CONCAT(contenttype) contenttypes
	FROM board b LEFT OUTER JOIN attach a ON b.bnum = a.pnum
	GROUP BY b.bnum
	ORDER BY b.bnum DESC
</select>
<!--검색기능을 사용한 목록창 -->
<select id="getsearch" resultType="java.util.Map" parameterType="java.util.Map">
  SELECT b.bnum, bname, title, content, date, hitnum,
         GROUP_CONCAT(a.num) AS fnums,
         GROUP_CONCAT(pnum) AS pnums,
         GROUP_CONCAT(fname) AS fnames,
         GROUP_CONCAT(fsize) AS fsizes,
         GROUP_CONCAT(contenttype) AS contenttypes
  FROM board b
  LEFT OUTER JOIN attach a ON b.bnum = a.pnum
  WHERE
    <choose>
      <when test="category == '번호' and key != ''">bnum = #{key}</when>
      <when test="category == '이름' and key != ''">bname = #{key}</when>
      <when test="category == '내용' and key != ''">content LIKE CONCAT('%', #{key}, '%')</when>
    </choose>
  GROUP BY b.bnum, b.bname, b.title, b.content, b.date, b.hitnum
  ORDER BY b.bnum DESC
</select>
```
<div style="font-size : 17px; margin-bottom: 7px">
데이터 베이스에 저장된 문의사항들을 불러옵니다. 여기서는 두개의 테이블을 불러와야 하기 때문에 JOIN을 사용했습니다.
</div>
<div style="font-size : 17px; margin-bottom: 15px">
검색기능을 사용해야 하므로 번호, 이름, 내용으로 찾을시 해당 목록이 나오게 만드는 SQL문입니다
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">BoardMapper와 BoardDAO</div>
```java
//Board Mapper
@Component("boardmapper")
@Mapper
public interface BoardMapper {
	
	public List<Map<String,String>> getList();

//Board DAO
public class BoardDao {
	@Autowired
	@Qualifier("boardmapper")
	private BoardMapper bm;
	
	public PageInfo<Map> getlist(int pagenum,int pagesize)
	{
		PageHelper.startPage(pagenum,pagesize);
		PageInfo<Map> PageInfo = new PageInfo<>(bm.getList());
		
		return PageInfo;
	}
	
	public PageInfo<Boardvo> getsearch(String category,String key,int pagenum,int pagesize) 
	{
		PageHelper.startPage(pagenum,pagesize);
		
		Map<String,String> map = new HashMap<>();
		map.put("category", category);
	    	map.put("key", key);
		
		PageInfo<Boardvo> pageinfo = new PageInfo<>(bm.getsearch(map));
		
		return pageinfo;
	}

```
<div style = "font-size : 15px; margin-bottom: 7px">
목록창에는 검색기능도 있어야 하므로 검색후 목록이 나올수 있게 추가했습니다.
</div>
<div style = "font-size : 15px; margin-bottom: 7px">
매우 많은 목록이 있다면 이용자가 한번에 보기 어려우므로 pageInfo를 사용해서 페이지별로 볼수 있는 기능도 추가했습니다. 목록에 가장 기본적으로 넣어야할 기능이 아닐까 생각합니다.
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
category는 번호, 이름, 내용 이고 key는 사용자가 입력한 값을 서버가 받아서 원하는 값을 웹페이지에 출력해줍니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">Board Controller</div>
```java
@Controller
@RequestMapping("/board")
@SessionAttributes("userid")
public class BoardController {
	@Autowired
	@Qualifier("boarddao")
	BoardDao bd;

	@GetMapping("/board/list/page/{pn}")
	public ModelAndView getlist(@SessionAttribute(name = "userid", required = false)String userid,@PathVariable int pn, Model model) {
		model.addAttribute("user",fairydao.getJoinedMem(userid));
	    int pagenum = pn;
	    int pagesize = 10;
	    ModelAndView modelAndView = new ModelAndView("board/list");
	    PageInfo<Map> pageinfo = bd.getlist(pagenum, pagesize);
	    model.addAttribute("pageinfo", pageinfo);
	    return modelAndView;
	}

	@GetMapping("/board/search/page/{pn}")
	public ModelAndView getsearch(@RequestParam("category")String category,
								  @RequestParam("key")String key,Model model,
								  @PathVariable int pn)
	{
		int pagenum = pn;
	    int pagesize = 10;
		ModelAndView modelAndView = new ModelAndView("board/searchform");
		PageInfo<Boardvo> pageinfo = bd.getsearch(category,key,pagenum, pagesize);
	    model.addAttribute("pageinfo", pageinfo);
	    return modelAndView;
	}
```
<div style="font-size : 17px; margin-bottom: 7px">
웹페이지에 출력만 하면 되므로 get방식만을 사용했습니다.
</div>
<div style="font-size : 17px; margin-bottom: 15px">
한 페이지당 10개의 목록만 출력하게 만들었습니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">LIST 페이지-jsp</div>
```html
<script type="text/javascript">
function showPasswordModal(bnum,pass) {
	  const modal = document.getElementById('passwordModal');
	  modal.style.display = 'block';

	  // 확인 버튼을 클릭했을 때 처리하는 함수
	  function checkPassword() {
	    const passwordInput = document.getElementById('passwordInput').value;
	    if (passwordInput.trim() === pass.toString()) {
	      // 비밀번호가 일치하면 해당 페이지로 이동합니다.
	      const detailURL = '/board/board/detail/' + bnum;
	      window.location.href = detailURL;
	    } else {
	      alert('비밀번호가 일치하지 않습니다.');
	    }
	    modal.style.display = 'none';
	  }
	  window.onclick = function(event) {
		    if (event.target === modal) {
		      modal.style.display = 'none';
		    }
		  };

		  // 사용자가 'Esc' 키를 누르면 대화상자를 닫습니다.
		  document.onkeydown = function(event) {
		    if (event.key === 'Escape' || event.key === 'Esc') {
		      modal.style.display = 'none';
		    }
		  };
	  // 확인 버튼 클릭 이벤트를 추가합니다.
	  const confirmButton = modal.querySelector('button');
	  confirmButton.onclick = function() {
	    checkPassword('${list.pass}');
	  };
	}
</script>
<h1>문의사항</h1>
<hr>
<table>
    <thead>
   
    <tr>
        <th>NO</th>
        <th>ID</th>
        <th>TITLE</th>
        <th>DATA</th>
        <th>HIT</th>
    </tr>
    </thead>

    <tbody>
    <c:set var="bnum" value="${0}"></c:set>
  
    <c:forEach var="list" items="${pageinfo.list}">
        <c:if test="${list.bnum != bnum}">
            <tr>
                <td>${list.bnum}</td>
                <td>${list.bname}</a></td>
                
<div id="passwordModal" style="display: none;">
  <div class="modal-content">
  <h2>비밀번호 입력</h2>
  <input type="text" id="passwordInput">
  <button onclick="checkPassword()">확인</button>
  </div>
</div>

<c:choose>
  <c:when test="${user.number == 9 || list.pass == 0}">
    <td><a href="/board/board/detail/${list.bnum}">${list.title}</a></td>
  </c:when>
  <c:otherwise>
    <td><a href="javascript:void(0);" onclick="showPasswordModal(${list.bnum},${list.pass})">비밀글 입니다</a></td>
  </c:otherwise>
</c:choose>	
           <td><fmt:formatDate value="${list.date}" pattern="yyyy-MM-dd"/></td>
           <td>${list.hitnum}</td>
     </tr>
        </c:if>
        <c:set var="bnum" value="${list.bnum}"></c:set>
    </c:forEach>
    
    </tbody>
</table>

<div id="page">
    <c:if test="${pageinfo.hasPreviousPage}">
        [<a href="/board/board/list/page/${pageinfo.prePage}">Previous</a>]
    </c:if>
    <c:forEach var="pageNum" items="${pageinfo.navigatepageNums}">
        <c:choose>
            <c:when test="${pageNum eq pageinfo.pageNum}">
                <strong style="color: red">${pageNum}</strong>
            </c:when>
            <c:otherwise>
                [<a href="/board/board/list/page/${pageNum}">${pageNum}</a>]
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${pageinfo.hasNextPage}">
        [<a href="/board/board/list/page/${pageinfo.nextPage}">Next</a>]
    </c:if>
</div>

<div id="page1">
    <c:if test="${pageinfo.pages gt 1}">
        [<a href="/board/board/list/page/1">FirstPage</a>]
        [<a href="/board/board/list/page/${pageinfo.pages}">LastPage</a>]
    </c:if>
</div>

<form id="div" action="/board/board/search/page/1" method="get">
    <select name="category">
        <option value="번호">번호</option>
        <option value="이름">이름</option>
        <option value="내용">내용</option>
    </select>
    <input type="text" name="key">
    <input type="submit" value="검색">
</form>
</body>
```

<div style="font-size : 17px; margin-bottom: 7px">
검색기능을 활용해서 내가 원하는 게시물을 찾을수 있습니다.<br>
(admin 계정으로 로그인해야 모든 title이 보이게 만듬)<br>
title에 a태그로 링크를 달아서 클릭하면 문의사항 디테일에 들어갈수 있게 만들었습니다.
<img src="/assets/images/list.png" width="800px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
<img src="/assets/images/num.png" width="800px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
<img src="/assets/images/name.png" width="800px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
<img src="/assets/images/test.png" width="800px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/><br>
</div>
<div style="font-size : 17px; margin-bottom: 15px">
문의 글을 보기위해선 비밀번호를 입력해야 합니다.<br>
(일반회원 화면)
<img src="/assets/images/pass.png" width="800px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div><br>

<hr>

- 디테일 페이지

<div style="font-size : 17px; color:blue; margin-bottom: 7px">BoardMapper.XML</div>
```html
<select id="getBoard" resultType="java.util.Map" parameterType="Integer">
	SELECT b.*, a.fname, a.fsize, fm.name AS name
FROM board b
LEFT OUTER JOIN attach a ON b.bnum = a.pnum
LEFT OUTER JOIN fairymem fm ON b.bname = fm.userid
WHERE b.bnum = #{bnum}
<!-- 원하는 문의사항글을 가져옴 -->
</select>

<update id="gethit" parameterType="com.ezen.spring.board.teampro.board2.Boardvo">
	UPDATE board SET hitnum = hitnum + 1 WHERE bnum = #{bnum}
</update>

<insert id="addcomment" parameterType="com.ezen.spring.board.teampro.board2.Comment">
INSERT INTO comment (bnum,name,comcontent,comdata) values(#{bnum},#{name},#{comcontent}, CURRENT_TIMESTAMP)
</insert>

<select id="comlist" resultType="com.ezen.spring.board.teampro.board2.Comment" parameterType="_int">
SELECT * FROM comment WHERE bnum = #{bnum}
</select>

<select id="getdownload" resultType="String" parameterType="Integer">
	SELECT fname, contenttype FROM attach WHERE num = #{num};
</select>

```

<div style = "font-size : 15px; margin-bottom: 7px">
update는 리스트에서 title을 누르면 문의사항글을 보여줌과 동시에 조회수가 올라갑니다.
</div>
<div style = "font-size : 15px; margin-bottom: 7px">
"addcomment"와"comlist"는 관리자가 댓글을 달고 웹화면에 출력하게 해줍니다.
</div>
<div style = "font-size : 15px; margin-bottom: 7px">
"getdownload"첨부파일 다운로드를 위한 SQL문 입니다.
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
두개의 테이블을 JOIN을 사용해서 가져옵니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">BoardMapper와 BoardDAO</div>
```java
//Board Mapper
@Component("boardmapper")
@Mapper
public interface BoardMapper {
	
	public List<Map<String,Object>> getBoard(int bnum);
	//이용자가 num을 주면 서버는 DB에서 해당 num을 가진 객체를 가져옴

	public int gethit(Boardvo vo);

	//↓관리자 댓글 생성
	public int addcomment(Comment vo); 
	
	public List<Comment> comlist(int bnum);
	
	//첨부파일 다운로드
	public String getdownload(int num);
//Board DAO
public class BoardDao {
	@Autowired
	@Qualifier("boardmapper")
	private BoardMapper bm;
	
	public List<Map<String,Object>> getboard(int bnum)
	{
		return bm.getBoard(bnum);
	}

	public boolean hitup(Boardvo vo)
	{
		return bm.gethit(vo)>0;
	}

	//↓관리자 댓글 생성
	public boolean addcomm(Comment vo)
	{
		boolean add = bm.addcomment(vo)>0;
		return add;
	}
	
	public List<Comment> getcomlist(int bnum)
	{
		 List<Comment> clist = bm.comlist(bnum);
		return clist;
	}

	public String getdownload(int num)
	{
		return bm.getdownload(num);
	}

	// 문의사항 삭제로직
	@Transactional
	public boolean delete(int bnum,Boardvo vo,HttpServletRequest request)
	{
		boolean bSaved = bm.attdalete(bnum)>0;
		boolean aSaved = bm.dalete(bnum)>0;

		ServletContext context = request.getServletContext();
		String savePath = context.getRealPath("files");
	    	File delFile = new File(savePath,fname);
	    	boolean fdel = delFile.delete();

		return bSaved&&aSaved;
	}

``
<div style = "font-size : 15px; margin-bottom: 15px">
이용자가 준 num을 가지고 DB에서 해당 객체를 찾아옵니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">Board Controller</div>
```java
@Controller
@RequestMapping("/board")
@SessionAttributes("userid")
public class BoardController {
	@Autowired
	@Qualifier("boarddao")
	BoardDao bd;

	@GetMapping("/board/detail/{bnum}")
	public ModelAndView deteil(@SessionAttribute(name = "userid", required = false)String userid,@PathVariable int bnum,Boardvo vo, Model model)
	{
		model.addAttribute("user",fairydao.getJoinedMem(userid));
		bd.hitup(vo);
		List<Map<String,Object>> blist = new ArrayList<>();
		ModelAndView modelAndView = new ModelAndView("board/detail");
		blist = bd.getboard(bnum);
		List<Comment> clist = bd.getcomlist(bnum);
		
		model.addAttribute("detail", blist);
		model.addAttribute("comlist", clist);
		return modelAndView;
	}

	//댓글기능 구현 로직
	@PostMapping("/comment/{bnum}")
	@ResponseBody
	public Map<String,Boolean> getadd(@PathVariable int bnum, Comment vo)
	{
		Map<String,Boolean> map = new HashMap<>();
		boolean save = bd.addcomm(vo);
		map.put("added", save);
		return map;
	}
	//첨부파일 다운로드 로직
	@GetMapping("/download/{num}")
	   public ResponseEntity<Resource> download(HttpServletRequest request,
	                           @PathVariable int num,String fname)
	   {
	      String filename = bd.getdownload(num);
	      Resource resource = resourceLoader.getResource("WEB-INF/files/"+filename);
	        String contentType = null;
	        try {
	            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 
	        if(contentType == null) {
	            contentType = "application/octet-stream";
	        }
	 
	        /* 파일명이 한글로 되어 있을 때 다운로드가 안되는 경우... */
	        try {
	         filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
	      } catch (UnsupportedEncodingException e) {
	         e.printStackTrace();
	      }

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
	                .body(resource);
	   }
	//문의사항 전체삭제
	@PostMapping("/board/remove/{bnum}")
	@ResponseBody
	public Map<String, Boolean> remove(@PathVariable int bnum,@SessionAttribute(name = "userid", required = false)String userid,
					   @RequestParam("fname")String fname,HttpServletRequest request)
		{
		Map<String, Boolean> map = new HashMap<>();
		map.put("remove", bd.delete(bnum,fname,request));
		//System.out.println("결과 = " + bd.delete(bnum, vo));
		
		return map;
		}
	
```
<div style="font-size : 17px; margin-bottom: 7px">
model에 담긴 "user"은 로그인한 이용자가 관리자 인지 아닌지를 판단하기 위해 추가 됬습니다.
</div>
<div style="font-size : 17px; margin-bottom: 7px">
리스트에서 title을 눌러 문의사항 글에 들어가면 가장 먼저 조회수가 1올라갑니다.
</div>
<div style="font-size : 17px; margin-bottom: 7px">
문의사항에 댓글을 달기위해 post방식으로 데이터를 전송을 받습니다. "comlist"는 등록한 댓글을 불러옵니다
</div>
<div style="font-size : 17px; margin-bottom: 15px">
첨부파일을 다운받을수 있게 로직을 만들었습니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">DETAIL 페이지-jsp</div>
```html
<script src="https://code.jquery.com/jquery-3.7.0.min.js" integrity="sha256-2Pmvv0kuTBOenSvLm6bvfBSSHrUJ+3A7x6P5Ebd07/g=" crossorigin="anonymous"></script>
  <script type="text/javascript">
    function remove(bnum) {
      var realAuthor = '${detail[0]['bname']}';
      var requester = '${userid}';
      if (realAuthor != requester) {
        alert('글 작성자만 삭제할 수 있어요~');
        return;
      }
      //글 올린 당사자만 삭제가 가능하게 만들었습니다.

      if (!confirm("정말로 삭제하시겠습니까?")) {
        return false;
      }
      $.ajax({
        url: '/board/board/remove/' + bnum,
        data: { "bnum": bnum },
        method: 'post',
        cache: false,
        dataType: 'json',
        success: function(res) {
          var message = res.remove ? "삭제 성공" : "삭제 실패";
          alert(message);
          location.href = "/board/board/list/page/1";
          if (!res.remove) {
            alert("작성자만 삭제할 수 있습니다.");
          }
        },
        error: function(err) {
          alert('에러: ' + err);
        }
      });
    }

    function goBack() {
      history.back();
    }

    function added(bnum) {
      if (!confirm("댓글 저장하시겠습니까?")) {
        return false;
      }
      $('#btnUpload').prop('disabled', true);
      var form = $('#addform').serialize();
      $.ajax({
        url: '/board/comment/' + bnum,
        data: form,
        method: 'post',
        cache: false,
        dataType: 'json',
        success: function(res) {
          var message = res.added ? "댓글 성공" : "댓글 실패";
          alert(message);
          if (res.added) {
        	  location.reload();
          }
        },
        error: function(err) {
          alert('에러: ' + err);
          $('#btnUpload').prop('disabled', false);
        }
      });
      return false;
    }
  </script>
   <script type="text/javascript">
   function updateform(bnum) {
	      var realAuthor = '${detail[0]['bname']}';
	      var requester = '${userid}';
	      if (realAuthor != requester) {
	        alert('글 작성자만 삭제할 수 있어요~');
	        return;
	      }
	      if (realAuthor = requester) {
	    	  location.href = "/board/board/update/"+bnum;
		      }
	     
	    }
   </script>
</head>
<body>
  <table>
    <h1>문의사항</h1>
    <hr>
    <div id="com">
      <a href="javascript:updateform(${detail[0].bnum})" class="btn">수정</a>
      <a href="javascript:remove(${detail[0].bnum});" class="btn">삭제</a>
    </div>
    <tr>
      <th>NO</th>
      <td>${detail[0]['bnum']}</td>
    </tr>
    <tr>
      <th>NAME</th>
      <td>${detail[0]['name']}</td>
    </tr>
    <tr>
      <th>TITLE</th>
      <td>${detail[0]['title']}</td>
    </tr>
    <c:choose>
    <c:when test="${not empty detail[0]['fname']}">
        <tr>
            <th>ATTACH</th>
            <td class="attach">
                <c:forEach var="detailItem" items="${detail}">
                    <a href='/board/download/${detailItem.num}'>${detailItem.fname}
                        <c:if test="${not empty detailItem.fname}">(${detailItem.fsize}k)</c:if>
                    </a><br>
                </c:forEach>
            </td>
        </tr>
    </c:when>
    <c:otherwise>
        <!-- If you want to handle the 'otherwise' case, you can add the content here -->
    </c:otherwise>
</c:choose>

    <tr>
      <th>CONTENT</th>
      <td class="content">${detail[0]['content']}</td>
    </tr>
    <tr>
      <th>DATA</th>
      <td><fmt:formatDate value="${detail[0]['date']}" pattern="yyyy-MM-dd"/></td>
    </tr>
    <tr>
      <th>HIT</th>
      <td>${detail[0]['hitnum']}</td>
    </tr>
  </table>

  <table>
    <c:forEach var="com" items="${comlist}">
      <tr class="divcom" style="width: 200px;">
        <th style="width: 100px; background-color: white; font-size: 13px;">${com.name}</th>
        <th style="width: 400px; padding: 5px; background-color: white; font-size: 13px;">${com.comcontent}</th>
        <th style="width: 100px; background-color: white; font-size: 13px;">${com.data}</th>
      </tr>
    </c:forEach>
  </table>
  
<c:if test="${user.number eq 9}">
  <form id="addform" class="comment-form">
    <input type="hidden" id="name" name="name" value="관리자">
    <div>
      <label for="content">댓글</label>
      <input type="text" id="comcontent" name="comcontent">
      <button id="btnUpload" type="button" onclick="added(${detail[0]['bnum']});" class="btn">확인</button>
    </div>
  </form>
</c:if>
```

<div style="font-size : 17px; margin-bottom: 15px">
댓글을 달수 있는건 관리자만 가능하기 때문에 댓글 등록은 관리자한테만 보이게 if문을  사용했습니다.
</div>
<div style="font-size : 17px; margin-bottom: 7px">
관리자로 로그인시에는 댓글을 달수있는 칸이 생깁니다
<img src="/assets/images/de2.png" width="800px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div><br>
<div style="font-size : 17px; margin-bottom: 7px">
관리자가 댓글을 달았다면 이런 화면이 보여집니다
<img src="/assets/images/de.png" width="800px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div><br>
<div style="font-size : 17px; margin-bottom: 7px">
첨부파일은 다운받을수 있게 만들었습니다.<br>
<img src="/assets/images/de3.png" width="800px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div><br>


<hr>
- 업데이트 페이지

<div style="font-size : 17px; color:blue; margin-bottom: 7px">BoardMapper.XML</div>
```html

//첨부파일 업데이트 
<update id="getupdate" parameterType="com.ezen.spring.board.teampro.board2.Boardvo">
    UPDATE board SET title=#{title}, content=#{content} WHERE bnum = #{bnum}
</update>

//문의사항 글 삭제	
<delete id="dalete" parameterType="com.ezen.spring.board.teampro.board2.Boardvo">
    DELETE FROM test.board WHERE bnum = #{bnum}
</delete>
//문의사항 첨부파일 삭제
<delete id="attdalete" parameterType="com.ezen.spring.board.teampro.board2.FileVO">
    DELETE FROM attach WHERE num = #{num}
</delete>
```
<div style = "font-size : 15px; margin-bottom: 7px">
문의사항 삭제시 두개의 테이블에 적용해야 하기 때문에 두 테이블 삭제 SQL문을 만들었습니다.
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
업데이트는 글의 제목과 내용만 수정하게 만들었습니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">BoardMapper와 BoardDAO</div>
```java
//Board Mapper
@Component("boardmapper")
@Mapper
public interface BoardMapper {
	
	public int getupdate(Boardvo vo);
	
	public int dalete(int bnum);
	
	public int attdalete(int bnum);

//Board DAO
public class BoardDao {
	@Autowired
	@Qualifier("boardmapper")
	private BoardMapper bm;
	
	public boolean update(Boardvo vo) {
		boolean board = bm.getupdate(vo) > 0;
		
		int pnum = vo.getBnum();
	    List<FileVO> list = vo.getFlist();
	    if (list != null && !list.isEmpty()) {
	        for (int i = 0; i < list.size(); i++) {
	            list.get(i).setPnum(pnum);
	        }
	       boolean att = bm.addfile(vo.getFlist()) > 0;
	      }
	     
	    return board;
	}
	
	//첨부파일만 삭제로직
	@Transactional
	public boolean attdelete(int num,String fname,HttpServletRequest request )
	{
		boolean delatt = bm.attdalete(num)>0;
		ServletContext context = request.getServletContext();
	    String savePath = context.getRealPath("files");
	    File delFile = new File(savePath,fname);
	    boolean fdel = delFile.delete();
	    
		return delatt&&fdel;
	}


```
<div style = "font-size : 15px; margin-bottom: 7px">
파일만 업데이트가 가능하고, 첨부파일을 제외한 제목, 내용만 수정이 가능하게 만들었습니다. 
</div>
<div style = "font-size : 15px; margin-bottom: 15px">
잘못올린 파일 이라면 웹화면에서 삭제후 다른 첨부파일로 업로드가 가능하게 만들었습니다.
</div>

<div style="font-size : 17px; color:blue; margin-bottom: 7px">Board Controller</div>
```java
@Controller
@RequestMapping("/board")
@SessionAttributes("userid")
public class BoardController {
	@Autowired
	@Qualifier("boarddao")
	BoardDao bd;

	@PostMapping("/board/updatedd")
	   @ResponseBody
	   public Map<String, Object> getupdate(@RequestParam(value ="files",required=false) MultipartFile[] mfiles,
	                               @SessionAttribute(name = "userid", required = false)String userid, 
	                               Boardvo bo,@RequestParam("bnum") int bnum)
	   {
	      
	      Map<String, Object> map = new HashMap<>();
	      ServletContext context = request.getServletContext();
	       String savePath = context.getRealPath("/files");
	      List<FileVO> fileList = new ArrayList<>();
	          try {
	                for(int i=0;i<mfiles.length;i++) {
	                   if (mfiles[i].getSize() == 0) continue;  
	                   
	                     String fileName = mfiles[i].getOriginalFilename();
	                     mfiles[i].transferTo(new File(savePath + "/" + fileName));
	                     String cType = mfiles[i].getContentType();
	                     String pName = mfiles[i].getName();
	                     Resource res = mfiles[i].getResource();
	                     long fSize = mfiles[i].getSize();
	                     boolean empty = mfiles[i].isEmpty();
	                 
	                     FileVO vo = new FileVO();
	                     
	                     vo.setPnum(bnum);
	                     vo.setFname(fileName);
	                     vo.setFsize(fSize/1024);
	                     vo.setContentType(cType);
	                     
	                     fileList.add(vo);
	                     }
	                  bo.setFlist(fileList);
	                 boolean added = bd.update(bo);
	                 map.put("update", added); 
	          } catch (Exception e) {
	              e.printStackTrace();
	          }
	      return map;
	   }

	//첨부물만 삭제
	@PostMapping("/board/attremove")
	@ResponseBody
	public Map<String, Boolean> remove(@RequestParam("num")int num,
										@RequestParam("fname")String fname,HttpServletRequest request)
	{
		Map<String, Boolean> map = new HashMap<>();
		map.put("attremove", bd.attdelete(num,fname,request));

		return map;
	}
```
<div style="font-size : 17px; margin-bottom: 15px">
업데이트 로직은 추가 로직과 유사합니다.
</div>


<div style="font-size : 17px; color:blue; margin-bottom: 7px">Updataform 페이지-jsp</div>
```html

function attremove(num,fname){
if (!confirm("파일을 삭제겠습니까?")) {
    return false;
}
var obj = {};
obj.num = num;
obj.fname = fname;

$.ajax({
    url: '/board/board/attremove',// URL에 변수 값을 추가합니다.
    data:obj,
    method: 'post',
    cache: false,
    dataType: 'json',  // dataType을 'html'로 변경
    success: function(res) {
    	var message = res.attremove ? "삭제 성공" : "삭제 실패";
        alert(message);
        location.href ="/board/board/update/" + ${alist[0].bnum};
        if(!res.update){
        	location.reload();
        }
    },
    error: function(err) {
        alert('에러: ' + err);
    }
});
}

function update() {
	 $('#btnUpload').prop('disabled', true);
	var realAuthor = '${alist[0].bname}';
	var requester = '${userid}';

	if(realAuthor!=requester) {
		alert('글 작성자만 글을 수정할 수 있어요~');
		return;
	}
	
	if (!confirm("정말로 수정하시겠습니까?")) {
        return false;
    }
	var form = $('#updateform')[0];
	var formData = new FormData(form);
	$.ajax({
        url: '/board/board/updatedd', // URL에 변수 값을 추가합니다.
        data: formData,
        method: 'post',
        enctype:'multipart/form-data',
        cache: false,
        dataType: 'json',  // dataType을 'html'로 변경
        processData:false,//파일이 들어가니까 폼필드처럼 취급하지 말라 라는 뜻
		contentType:false,//파일이 들어가니까 폼필드처럼 취급하지 말라 라는 뜻
		timeout:3600,
        success: function(res) {
        	var message = res.update ? "수정 성공" : "수정 실패";
            alert(message);
            location.href = "/board/board/detail/${alist[0].bnum}";
            if(!res.update){
            	location.href ="/board/board/update/" + '${alist[0].bnum}';
            }
        },
        error: function(err) {
            alert('에러: ' + err);
            $('#btnUpload').prop('disabled', false);
        }
    });
  return false;
}
</script>
<meta charset="utf-8">
<title>UPDATEFORM</title>
</head>
<body>

<h1>회원정보 수정</h1><hr>
<div>
<form id="updateform" onsubmit="return update();">
<input type="hidden" name="bnum" value ="${alist[0].bnum}">
<table>
<tr><th>NO</th> <td>${alist[0].bnum}</td></tr>
<tr><th>NAME</th> <td>${alist[0].bname}</td></tr>
<tr><th>TITLE</th> <td><input type="text" id="title" name="title" value ="${alist[0].title}"></td></tr>
<tr><th>CONTENT</th> <td><input  type="text" id="content" name="content" value ="${alist[0].content}"></td></tr>
<tr><th>DATE</th> <td><fmt:formatDate value="${alist[0].date}" pattern="yyyy-MM-dd"/></td></tr>
<tr><th>HIT</th> <td>${alist[0].hitnum}</td></tr>
<tr><th>ATTACH</th><td>
<c:forEach var="atb" items="${alist}">
${atb.fname}
<a href="javascript:attremove('${atb.num}','${atb.fname}');" title="첨부파일 삭제">
<c:if test="${not empty atb.fname}">[X] <br><!-- 삭제를 원할시 X버튼 클릭 -->
</c:if>
</a>
<c:if test="${empty atb.fname}">
<input type="file" name="files" multiple="multiple" >
</c:if>

</c:forEach></td></tr>

</table>
<td><div><button id="btnUpload" type="submit">ENTER</button></div></td>
</form>
```
<div style="font-size : 17px; margin-bottom: 7px">
제목과 내용만 수정하거나 파일만 추가하거나 아니면 둘다 가능하게 만들었습니다.
<img src="/assets/images/up1.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>

<div style="font-size : 17px; margin-bottom: 7px">
X표시를 누르면 첨부물도 정상삭제가 됩니다.
</div>
<img src="/assets/images/up2.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/><br>
<hr>
<div style="font-size : 17px; margin-bottom: 7px">
이렇게 이용자의 문의사항을 만들어 보았습니다.
</div>

<h3>개발 환경</h3>
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">언어</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Java(JDK17), HTML/CSS/JSP, JavaScript, Python</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">서버</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Apache Tomcat10.1</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">프레임워크</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Spring Boot,MyBatis</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">DB</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">MySQL8.0</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">IDE</span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Eclipse</span></div>

<div style ="margin-bottom: 10px; font-size : 15px; "><span style="font-weight: bold;">API, 라이브러리</span>&nbsp;&nbsp;<span style="color:gray;">Jquery, Maven</span></div>
