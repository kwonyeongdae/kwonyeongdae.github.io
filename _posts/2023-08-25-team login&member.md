---
title:  "첫번째 팀 프로젝트 - 로그인&회원관리 페이지"
excerpt: "아동용 책판매 쇼핑몰 - 로그인&회원관리"

categories:
  - team preject
tags:
  - java
  - spring
  - jsp
  - MySQL
last_modified_at: 2023-08-25 19:23PM
---
<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발 기간</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 2023-07 ~ 2023-09(3개월)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">플랫폼</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">Web</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">개발인원</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">3명(팀장)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold;">담당 역할</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray"> 로그인 및 회원 관리(기여도 30%) 프론트엔드 -jsp(기여도 30%)</span></div>

<div style = "font-size : 15px; margin-bottom: 10px;"><span style="font-weight: bold; ">전체 코드&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><a href="https://github.com/kwonyeongdae/portfolio/tree/main/Book" style="text-decoration: none;"> github 보러가기</a></div>
----------------------------------------------------
<h3>프로젝트</h3>

- 회원가입기능
<div style="font-size : 17px; color:bule; margin-bottom: 7px">MemberVO</div>
```java
public class MemberVO 
{
	private int fnum;
	private String userid;
	private String pass;
	private String name;
	private String phone;
	private String email;
	private String birth; //생년월일
	private String gender; 
	//약식 동의시 숫자 1 비동의시 0으로 표기됨
	private boolean agrStipulation1;
   	private boolean agrStipulation2;
    	private boolean agrStipulation3;
	//회원 등급을 나타냄
   	private int number;
	//중고책 판매시 가격의 10%를 제외한 금액을 carrotpoint로 돌려줌
    	private int carrotpoint;
	//주소지 입력
    	private String postal_code;
    	private String contact_address;
    	private String detailed_address;
	//책 구매후 마일리지 적립
    	private int mileage;
}
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 회원가입은 보통의 쇼핑몰과 다르지 않습니다.</div>
<div style = "font-size : 15px; margin-bottom: 15px">
2. number는 회원의 등급을 나타냅니다 0은 일반회원 5는 휴먼계정 9는 관리자 계정으로 했습니다.</div>

<div style="font-size : 17px; color:bule; margin-bottom: 7px">FairyMapper.xml</div>
```html
<insert id="addfairyMem"
     parameterType="com.ezen.spring.board.teampro.login.MemberVO">
     INSERT INTO fairymem (fnum, userid, pass, name, phone, email, postal_code, contact_address, detailed_address, birth, gender, agrStipulation1, agrStipulation2, agrStipulation3, number,carrotpoint,mileage) 
     VALUES(NULL, #{userid}, #{pass}, #{name}, #{phone}, #{email}, #{postal_code}, #{contact_address}, #{detailed_address}, #{birth}, #{gender}, #{agrStipulation1}, #{agrStipulation2}, #{agrStipulation3}, 0,#{carrotpoint},0)
</insert>
<!-- 아이디 중복 체크 -->
<select id="joinIdCheck" 
   parameterType="com.ezen.spring.board.teampro.login.MemberVO" resultType="Integer">
   SELECT COUNT(*) FROM fairymem WHERE userid = #{userid}
</select>
<!-- 이메일 중복 체크 -->
<select id="joinEmailCheck" 
   parameterType="com.ezen.spring.board.teampro.login.MemberVO" resultType="Integer">
   SELECT COUNT(*) FROM fairymem WHERE email = #{email}
</select> 
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 회원가입시 아이디,이메일등 중복체크를 위한 sql문이 존재합니다.</div>

<div style="font-size : 17px; color:bule; margin-bottom: 7px">FairyController</div>
```java
@Controller
@RequestMapping("/fairy")
@SessionAttributes("userid")
public class FairyController 
{

  @Autowired
  @Qualifier("fairydao")
  private FairyDAO fairydao;
  
  @PostMapping("/join")
  @ResponseBody 
  public Map<String, Boolean> addJoin(MemberVO mem) {
	  Map<String, Boolean> map = new HashMap();
	  map.put("addedJoin", fairydao.addJoin(mem));
	  return map;  
  }
  
  @GetMapping("/join")
  @ResponseBody 
  public Map<String, Boolean> joinCheck(MemberVO mem) {
	  Map<String, Boolean> map = new HashMap();
	  map.put("isDuplicateId", fairydao.joinIdCheck(mem));
	  map.put("isDuplicateEmail", fairydao.joinEmailCheck(mem));
	  return map;
  }
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 회원가입 로직과 중복체크 로직은 따로 분리했습니다.</div>

<div style="font-size : 17px; color:bule; margin-bottom: 7px">Joinfrom.jsp</div>
```html
var isIdChecked = false;
var isEmailChecked = false;

function form_check() {
	  
  var userid = document.getElementById("userid").value;
  var pass = document.getElementById("pass").value;
  var passconfirm  = document.getElementById("pass-confirm").value;
  var name = document.getElementById("name").value;
  var phone1 = document.getElementById("phone1").value;
  var phone2 = document.getElementById("phone2").value;
  var phone3 = document.getElementById("phone3").value;
  var phone = phone1 + '-' + phone2 + '-' + phone3;
  var email = document.getElementById("email").value;
  var postal_code = document.getElementById("postal_code").value;
  var contact_address= document.getElementById("contact_address").value;
  var detailed_address = document.getElementById("detailed_address").value;
  var genderInput = document.querySelector('input[name="gender"]:checked');
  var birth = document.getElementById("birth").value;
  var agrStipulation1 = document.getElementById("agrStipulation1").checked;
  var agrStipulation2 = document.getElementById("agrStipulation2").checked;
  var agrStipulation3 = document.getElementById("agrStipulation3").checked;
   
  if (userid == "") {
    alert("'아이디' 입력은 필수입니다");
    return false;
    
  } else if (pass == ""||passconfirm == "") {
    alert("'암호' 입력은 필수입니다");
    return false;
  } else if (name == "") {
    alert("'이름' 입력은 필수입니다");
    return false;
  } else if (phone1 == "" || phone2 == "" || phone3 == "") {
    alert("'전화' 입력은 필수입니다");
    return false;
  } else if (!/^\d{3}$/.test(phone1) || !/^\d{4}$/.test(phone2) || !/^\d{4}$/.test(phone3)) {
    alert("'전화' 형식이 올바르지 않습니다");
    return false;
  } else if (email == "") {
    alert("'이메일' 입력은 필수입니다");
    return false;
  } else if (!genderInput) {
	alert("'성별' 선택은 필수입니다");
	return false;
  } else if (!agrStipulation1) {
    alert("'이용약관'에 동의해야 합니다");
    return false;
  } else if (!agrStipulation2) {
    alert("'개인 정보 수집 및 이용'에 동의해야 합니다");
    return false;
  }
 ```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 필수사항 입력 안할시에는 안내문구가 뜹니다.
<img src="/assets/images/id.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>
```html
  // 아이디 검증
  var userIdRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{4,12}$/;
  if (!userIdRegex.test(userid)) {
    alert("아이디는 4자~12자의 영문자와 숫자로 입력해주세요.");
    return false;
  }

  // 비밀번호 검증
  if (password !== confirmPassword) {
      mismatchMessage.textContent = '비밀번호가 일치하지 않습니다.';
      mismatchMessage.style.display = 'block';
    } else if (!passwordRegex.test(password)) {
      mismatchMessage.textContent = '영문 대소문자/숫자/특수문자를 혼용하여 8~16자 입력해주세요.';
      mismatchMessage.style.display = 'block';
    } else {
      mismatchMessage.style.display = 'none';
    }
  }
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 아이디와 비밀번호가 조건에 안맞을시 오류문구가 뜹니다.
<img src="/assets/images/alert.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>
```html
var isIdChecked = false;
var isEmailChecked = false;

if (!isIdChecked) {
	    alert("'아이디 중복체크'를 확인해주세요");
	    return false;
	  }
  
  if (!isEmailChecked) {
	    alert("'이메일 중복체크'를 확인해주세요");
	    return false;
	  }
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 아이디와 이메일은 중복체를 해야합니다.
<img src="/assets/images/idc.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>
<img src="/assets/images/join.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
<div style = "font-size : 15px; margin-bottom: 15px">
1. 모든 입력란을 채우면 회원가입이 됩니다.
</div>

-로그인기능
<div style="font-size : 17px; color:bule; margin-bottom: 7px">FairyMapper.xml</div>
```html
<select id="loginCheck" parameterType="com.ezen.spring.board.teampro.login.MemberVO" resultType="com.ezen.spring.board.teampro.login.MemberVO">
      SELECT number FROM fairymem WHERE userid = BINARY #{userid} AND pass = BINARY #{pass}
</select>
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 아이디와 비밀번호는 대소문자를 정확히 입력해야 합니다.
</div>

```java
@PostMapping("/login")
@ResponseBody
   public Map<String, Boolean> login(@RequestParam("userid") String userid, @RequestParam("pass") 				     String pass, HttpSession session, MemberVO mem, Model model)
    {
	  mem = new MemberVO();
     mem.setUserid(userid);
     mem.setPass(pass);
      boolean login = fairydao.login(mem);
   
      if(login){
        model.addAttribute("userid", mem.getUserid());
        session.setAttribute("userid", mem.getUserid());
        session.setAttribute("isAdmin", mem.getNumber());
      }
      boolean signupRequired = !fairydao.userExists(mem); 
      Map<String, Boolean> map = new HashMap<>();
      map.put("login", login);
      map.put("signupRequired", signupRequired);
      return map;
    }
```
<div style = "font-size : 15px; margin-bottom: 15px">
1. 입력받은 아이디와 비밀번호는 SQL문으로 전달됩니다.
</div>

<div style="font-size : 17px; color:bule; margin-bottom: 7px">Loginfrom.jsp</div>
```html
function login(){
	
	var info = $('#loginForm').serialize();
	$.ajax({
		url:'/fairy/login',
		method:'post',
		data:info,
		cache:false,
		dataType:'json',
		success:function(res){
			 if (res.login) {
                 alert('로그인 성공.\n오늘도 행복하세요^^');
                 location.href = '/book/list/page/1';
             } else {
                 if (res.signupRequired) {
                     if (confirm('회원가입이 필요합니다. 회원가입 하시겠습니까?')) {
                         location.href = '/fairy/joinForm';
                     }
                 } else {
                     alert('아이디와 비밀번호가 일치하지 않습니다.\n다시 입력해주세요.');
                 }
             }
         },
		error:function(xhr,status,err){
		      alert('로그인에 실패하였습니다.\n관리자에게 문의 바랍니다.'+err);
			console.log(xhr.responseText);
		}
	});
	return false;
}
```
<div style = "font-size : 15px; margin-bottom: 7px">
1. 회원이 아닌 아이디를 입력한다면 '회원가입이 필요합니다'라는 문구가 뜹니다.
<img src="/assets/images/login.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>
<div style = "font-size : 15px; margin-bottom: 7px">
2. 아이디와 비밀번호를 정확히 입력한다면 로그인 성공 문구가 뜹니다.
<img src="/assets/images/log.png" width="400px" height="250px" title="px(픽셀) 크기 설정" alt="RubberDuck"/>
</div>