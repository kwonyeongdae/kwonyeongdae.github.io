...java
package com.ezen.spring.board.teampro.carrotmarket;
import org.springframework.web.bind.annotation.CookieValue;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.spring.board.teampro.book.Book;
import com.ezen.spring.board.teampro.cart.BookVO;
import com.github.pagehelper.PageInfo;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carrot")
@SessionAttributes("userid")
public class CarrotController {

	@Autowired
	@Qualifier("carrotdao")
	CarrotDAO dao;
//========================================carrot물건 추가로직 ================================================	
	@GetMapping("/add")
	public String add()
	{
		return "thymeleaf/carrot/addform";
	}

	@PostMapping("/add")
    @ResponseBody
       public Map<String, Object> addcarrot(@SessionAttribute(name = "userid", required = false)String userid,
    		   								@RequestParam(value ="file1", required=false) MultipartFile cvrimg,
                                  		    @RequestParam(value ="file2", required=false) MultipartFile[] contimg1,
                                  		    CarrotVO cv,HttpServletRequest request) 
      {
		
		System.out.println("여기 ca = " +cv);
		System.out.println("여기 userid = " +userid);
		System.out.println("여기 cvrimg = " +cvrimg);
		System.out.println("여기 contimg1 = " +contimg1);
		
      Map<String, Object> map = new HashMap<>();
      ServletContext context = request.getServletContext();
      String savePath = context.getRealPath("/carrot");
      List<CarrotAttach> filelist = new ArrayList<>();
     
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
	//========================================carrot물건 추가로직 ================================================	
	
	//========================================carrot물건 list로직 ================================================	
	@GetMapping("/list/page/{pn}")
	   public String getlist(@PathVariable int pn,
	         @RequestParam(value="category", required=false) String category,
	         @RequestParam(value="keyword", required=false) String keyword, Model model,
	         @SessionAttribute(name = "userid", required = false)String userid
	         )
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
	public String search( @RequestParam("category") String category,
						 @RequestParam("keyword") String keyword,
						 Model model)
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
	//========================================carrot물건 list로직 ================================================	
	@GetMapping("/detail/{cnum}")
	public String getcarrot(@PathVariable int cnum, Model model,CarrotVO cv,
							@SessionAttribute(name = "userid", required = false)String userid) {

	    List<Map> clist = dao.getcarrotandattach(cnum);
	    dao.hitup(cv);
	    List<Map> comlist = new ArrayList<>();
		comlist = dao.comment(cnum);
		
		List<Map> comtolist = new ArrayList<>();
		comtolist = dao.commenttocom();
	    for (Map item : clist) {
	        // contimg3와 contimg4를 null로 설정하거나, 빈 문자열로 설정합니다.
	    	if (item.get("contimg1") == null) {
	            item.put("contimg1", "");
	        }  
	    	if (item.get("contimg3") == null) {
	            item.put("contimg3", "");
	        }
	        if (item.get("contimg4") == null) {
	            item.put("contimg4", "");
	        }
	    }
	    model.addAttribute("comto", comtolist);
	    model.addAttribute("com", comlist);
	    model.addAttribute("detail", clist);
	    return "thymeleaf/carrot/detail";
	}
	//========================================carrot물건 수정로직 ================================================		
	@GetMapping("/updateform/{cnum}")
	public String updateform(@PathVariable int cnum, Model model) {
		List<Map> clist = dao.getcarrotandattach(cnum);
	
		  for (Map item : clist) {
		        // contimg3와 contimg4를 null로 설정하거나, 빈 문자열로 설정합니다.
			  if (item.get("contimg1") == null) {
		            item.put("contimg1", "");
		        }  
			  if (item.get("contimg3") == null) {
		            item.put("contimg3", "");
		        }
		        if (item.get("contimg4") == null) {
		            item.put("contimg4", "");
		        }
		    }
		model.addAttribute("up", clist);
		return "thymeleaf/carrot/updateform";
	}
	
	@PostMapping("/update")
    @ResponseBody
       public Map<String, Object> updatecarrot(@SessionAttribute(name = "userid", required = false)String userid,
    		   									@RequestParam(value ="file1", required=false) MultipartFile cvrimg,
    		   									@RequestParam(value ="file2", required=false) MultipartFile[] contimg1,
    		   									CarrotVO cv,HttpServletRequest request,@RequestParam int cnum) 
      {
		
		System.out.println("여기 ca = " +cv);
		System.out.println("여기 userid = " +userid);
		System.out.println("여기 cvrimg = " +cvrimg);
		System.out.println("여기 contimg1 = " +contimg1);
      Map<String, Object> map = new HashMap<>();
      ServletContext context = request.getServletContext();
      String savePath = context.getRealPath("/carrot");
      List<CarrotAttach> filelist = new ArrayList<>();
      
           try {	
        	   if(cvrimg != null && !cvrimg.isEmpty()) {
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
	//========================================carrot물건 수정로직 ================================================		
	
	//========================================carrot 댓글과 답글로직 ================================================	
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
	//========================================carrot 댓글과 답글로직 ================================================
	
	//========================================carrot 관심목록에 추가 로직 ================================================
	@PostMapping("/interest")
	@ResponseBody
	public Map<String,Boolean> interestadd(Interest in){
		Map<String,Boolean> map = new HashMap<>();
		System.out.println("여기" + in);
		map.put("inter", dao.interests(in));
		return map;
	}
	//========================================carrot 관심목록에 추가 로직 ================================================
	
	//========================================carrot 관심목록 list 로직 ================================================
	@GetMapping("/interestform")
	public String getinter(Model model,@SessionAttribute(name = "userid", required = false)String userid) {
		List<Map> ilist = new ArrayList<>();
		ilist = dao.get_interest_list(userid);
		model.addAttribute("inter",ilist);
		return "thymeleaf/carrot/interest";
	}
	//========================================carrot 관심목록 list 로직 ================================================
	
	//========================================carrot 구매으로 이동 로직 ================================================
	@PostMapping("/carrotbuy")
	@ResponseBody
	public String buyCart(@RequestParam int cnum, HttpServletRequest request) {
		HttpSession session = request.getSession();
	    // 기존에 저장된 "cartbuy" 속성 데이터를 가져와서 리스트에 추가
	    List<CarrotVO> existingList = (List<CarrotVO>) session.getAttribute("cartbuy");
	    if (existingList == null) {
	    	existingList = new ArrayList<>();
	    }
	    existingList.add(dao.session_carrot(cnum));
	    session.setAttribute("cartbuy", existingList);
	    
	    return "cart/buycart"; // 해당 뷰로 이동
	}
	//========================================carrot 구매으로 이동 로직 ================================================
	
	//========================================최종결제창에서 carrot 삭제 로직 ================================================
	@PostMapping("/carrot/del")
	@ResponseBody
	public Map<String, Object> buydelete(@RequestParam int cnum, HttpSession session) {
	    Map<String, Object> map = new HashMap<>();
	    boolean removed = false; 

	    List<CarrotVO> existingList = (List<CarrotVO>) session.getAttribute("cartbuy");
	    if (existingList != null) {
	        for (Iterator<CarrotVO> iterator = existingList.iterator(); iterator.hasNext();) {
	        	CarrotVO object = iterator.next();
	            if (object.getCnum()==cnum) {
	                iterator.remove();
	                removed = true;
	                break; // 원하는 객체를 찾았으면 더 이상 순회할 필요 없으므로 종료
	            }
	        }
	    }
	    map.put("remove", removed); // 제거 여부를 응답으로 전달
	    return map;
	}
	//========================================최종결제창에서 carrot 삭제 로직 ================================================
	
	//========================================최종결제창으로 이동 로직 ================================================
	@PostMapping("/odercarrot")
	@ResponseBody
	public Map<String, Object> pointupto(@RequestParam(value = "title[]") List<String> title,
	                                     @RequestParam(value = "price[]") List<Integer> price,
	                                     @RequestParam(value = "saleuserid[]") List<String> saleuserid,
	                                     @RequestParam(value = "cnum[]") List<Integer> cnum,
	                                     @SessionAttribute(name = "userid", required = false) String userid) {

	    Map<String, Object> paramMap = new HashMap<>();

	    if (userid != null) {
	        for (int i = 0; i < title.size(); i++) {
	            if (price.size() > i) {  // Check if carrotpoint list has an element at index i
	                boolean bought = dao.bought_pointdown(userid, title.get(i), price.get(i),saleuserid.get(i),cnum.get(i));
	                paramMap.put("oder", bought);
	                dao.stateTrans(saleuserid.get(i));
	            }
	        }
	    }
	    
	    return paramMap;
	}

}

...