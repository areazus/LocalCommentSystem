package areaz.us.bc.localCommentSystem;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import areaz.us.bc.localCommentSystem.models.AddCounselorsFormData;
import areaz.us.bc.localCommentSystem.models.Comment;
import areaz.us.bc.localCommentSystem.models.CommentFormData;
import areaz.us.bc.localCommentSystem.models.IfThenFormData;
import areaz.us.bc.localCommentSystem.models.User;
import areaz.us.bc.localCommentSystem.mysql.MYSQLReader;

@Controller
public class MainController{

	
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    
    @RequestMapping("/login")
    public String login(Model model) {
        //model.addAttribute("name", name);
        return "login";
    }
    
    @RequestMapping(value="/comments", method=RequestMethod.GET)
    public String commentsGet(@RequestParam(value="EMPLID", required=true, defaultValue="") String EMPLID, Model model) {
    	return commentsRequest(EMPLID, model, null);
    }
    
    
    @RequestMapping(value="/comments", method=RequestMethod.POST)
    public String commentsPost(@ModelAttribute("commentFormData") CommentFormData commentFormData, @RequestParam(value="EMPLID", required=true, defaultValue="") String EMPLID, Model model){
    	Comment comment = null;
    	if(commentFormData.getComment().trim().length()>0){
        	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		comment = new Comment(commentFormData.getEmplID(), new Timestamp(System.currentTimeMillis()), commentFormData.getComment().trim(), user.userID, commentFormData.getYear());
    	}

    	return commentsRequest(commentFormData.getEmplID()+"", model, comment);
    }
    
    public String commentsRequest(String EMPLID, Model model, Comment comment) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	model.addAttribute("name", auth.getName());	
    	model.addAttribute("EmplIDText", EMPLID);	
    	model.addAttribute("commentFormData", new CommentFormData());
    	try{       
        	return returnComment(Integer.parseInt(EMPLID.trim()), comment, model);
        }catch(Exception e){
        }
    	return "main";
    
    }
    
    @RequestMapping("/main")
    public String main(@RequestParam(value="EMPLID", required=false, defaultValue="") String EMPLID, Model model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	model.addAttribute("name", auth.getName());	
        try{
            return returnComment(Integer.parseInt(EMPLID.trim()), null, model);
        }catch(Exception e){
        }
    	return "main";
    }
    
    @RequestMapping("/profile")
    public String profile(@RequestParam(value="command", required=false, defaultValue="") String command, @RequestParam(value="argument", required=false, defaultValue="") String argument, Model model) {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();    	
    	try{
    		switch(Integer.parseInt(command.trim())){
    			case 1:
    				List<Comment> comments = MYSQLReader.getCommentsByUser(user.userID);
    				model.addAttribute("name", user.preferredName);	
    		    	model.addAttribute("EmplIDText", "Placed by " +user.userID);	
    				model.addAttribute("comments", comments);
    		    	return "commentsAdmin";
    			case 2:
    			{
    				if(user.privileges.isAdmin){
    					if(argument.trim().startsWith("*")){
    						List<Comment> comments1 = MYSQLReader.getAllComments();
    	    				model.addAttribute("name", user.preferredName);	
    	    		    	model.addAttribute("EmplIDText", "Placed by Everyone");	
    						model.addAttribute("comments", comments1);
    						
    	    		    	return "commentsAdmin";
    					}else{
    						List<Comment> comments1 = new LinkedList<>();
    						String[] nums = argument.trim().split("\\|");
    						for(String s : nums){
    							int uid = Integer.parseInt(s);
    							comments1.addAll(MYSQLReader.getCommentsByUser(uid));
    						}
    	    				model.addAttribute("name", user.preferredName);	
    	    		    	model.addAttribute("EmplIDText", "Placed by "+argument);	
    						model.addAttribute("comments", comments1);
    	    		    	return "commentsAdmin";
    					}
    				}
    			}
  
    		}
    	}catch(Exception e){
    	}
    	
    	if(user.privileges.isAdmin){
    		model.addAttribute("superUser", true);	
    	}
    	if(user.privileges.isSuperAdmin){
    		model.addAttribute("superAdmin", true);	
    	}
    	return "profile";
    }
    
    @RequestMapping("/logout")
    public String logout(Model model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	auth.setAuthenticated(false);
    	return "redirect:/login?logout";
    }
    
    
    @RequestMapping("/")
    public String root(@RequestParam(value="EMPLID", required=false, defaultValue="") String EMPLID, Model model) {
    	return main(EMPLID, model);
    }
    
    @RequestMapping(value="/IfThen", method=RequestMethod.GET)
    public String ifThenGet(@RequestParam(value="d", required=false, defaultValue="")String toDelete, Model model) {    	
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    	try{
    		MYSQLReader.deleteIfThen(user.userID, Integer.parseInt(toDelete));
    	}catch(Exception e){
        }

    	return returnIfThen(model);
    }
    
    @RequestMapping(value="/IfThen", method=RequestMethod.POST)
    public String ifThenPost(@ModelAttribute("ifThenFormData") IfThenFormData form, Model model) {
    	if(form != null && form.getValue().length()>0 && form.getEqualsString().length()>0){
        	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		MYSQLReader.addIfThen(user.userID, Integer.parseInt(form.getField()), form.getValue(), Integer.parseInt(form.getCondition()), Integer.parseInt(form.getColor()), form.getEqualsString());
    	}
    	
    	
    	return returnIfThen(model);
    }
    
    
    
    public String returnIfThen(Model model){
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	model.addAttribute("name", user.preferredName);
		model.addAttribute("ifThenFormData", new IfThenFormData());
    	model.addAttribute("ifthens", MYSQLReader.getIfThenByEmplID(user.userID));
    	return "IfThen";
    }
    
    public String returnComment(int emplId, Comment c, Model model){
    	
    	model.addAttribute("Counselor", MYSQLReader.getCounselor(emplId));
    	if(c!=null){
    		MYSQLReader.addComment(c);
    	}
    	List<Comment> comments = MYSQLReader.getCommentsByEmplID(emplId);
    	model.addAttribute("comments", comments);
    	return "comments";
    }

    
    @RequestMapping(value="/addcounselors", method=RequestMethod.GET)
    public String addCounselorsGet(Model model) {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if(!user.privileges.isSuperAdmin){
    		return "main";
    	}
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	model.addAttribute("name", auth.getName());	
    	return addCounselors(model);
    }

    @RequestMapping(value="/addcounselors", method=RequestMethod.POST)
    public String addCounselorsPost(@ModelAttribute("addCounselorsFormData") AddCounselorsFormData form, Model model) {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if(!user.privileges.isSuperAdmin){
    		return "main";
    	}
        boolean status = MYSQLReader.addCounslors(form);
        if(!status){
        	model.addAttribute("error", true);	
        }else{
        	model.addAttribute("success", true);	

        }
    	return addCounselors(model);
    }
    
    
    public String addCounselors(Model model){
    	model.addAttribute("addCounselorsFormData", new AddCounselorsFormData());	
    	return "addcounselors";
    	
    }
    
    
}
