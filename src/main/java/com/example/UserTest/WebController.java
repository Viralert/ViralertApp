package com.example.UserTest;

// done with the basic web app!
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

@Controller
public class WebController {
	public static User user;
	public boolean shouldGeocode = false;

	// figure out how to fix duplicate signup issue - causes crash
	@PostMapping("/signup.html")
	public String userSubmit(@ModelAttribute User user) {
//		System.out.println(user.getLocation());
//		System.out.println(user.getUsername());
//		System.out.println(user.getPassword());
//		boolean write = true;
		WebController.user = user;
		FileWriter fw;
		PrintWriter writer;
		Scanner sc;
		File users = new File("Users.txt");
		try{
			fw = new FileWriter(users, true);
			writer = new PrintWriter(fw);
			sc = new Scanner(users);
			String line = sc.nextLine();
			Scanner sc2;
			while(sc.hasNextLine()){
				sc2 = new Scanner(line);
				String username = sc2.next();
				String password = sc2.next();
				String location = sc2.next();
				if(username.equals(user.getUsername())) {
					// should be return "signup-error.html" once Vaibhav makes it
					return "signup.html";
//					write = false;
//					break;
				}
				line = sc.nextLine();
			}
			/*if (write) */writer.println(user.getUsername() + " " + user.getPassword() + " " + user.getLocation());
			sc.close();
			writer.close();
			fw.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		FileWriter fw2;
		PrintWriter locWriter;
		Scanner sc3;
		File locs = new File("Locations.txt");
		try{
			fw2 = new FileWriter(locs, true);
			locWriter = new PrintWriter(fw2);
			sc3 = new Scanner(locs);
			boolean write = true;
			while(sc3.hasNextLine()){
				String location = sc3.nextLine();
				String stats = sc3.nextLine();
				if(location.equals(user.getLocation())){
					write = false;
					break;
				}
			}
			if (write){
				locWriter.println(user.getLocation());
				locWriter.println("0");
			}
			sc3.close();
			locWriter.close();
			fw2.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		UserTestApplication.restart();
		return "index.html";
	}

	@RequestMapping(value={"/", "/index.html"})
	public String index() {
		UserTestApplication.restart();
		return "index.html";
	}

	// Login form
	@RequestMapping("/login.html")
	public String login() {
		return "login.html";
	}

	// Login form with error
	@RequestMapping("/login-error.html")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login.html";
	}

	@GetMapping("/signup.html")
	public String signup(Model model){
		model.addAttribute("user", new User());
		return "signup";
	}

	@RequestMapping(value = "/map.html", method = RequestMethod.POST)
	public ModelAndView reportSave(@ModelAttribute Location location, Map<String, Object> model){
		String status = location.getStatus();
		System.out.println(status);
		String location2 = "10159 Parish Place, Cupertino, CA 95014";
		if(user != null) location2 = user.getLocation();
		Scanner sc;
		if(status.equals("symptoms") || status.equals("positive")){
			try{
				sc = new Scanner(new File("Locations.txt"));
				while(sc.hasNextLine() && !sc.nextLine().equals(location2)) {sc.nextLine();}
				String num = sc.next();
				num = num.replace(num, Integer.toString(Integer.parseInt(num) + 1));
				System.out.println(num);
				sc.close();
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		UserTestApplication.restart();
		String loc = "10165 Parish Place, Cupertino, CA 95014";
		if(WebController.user == null){
			Scanner scanner = null;
			try {
				scanner = new Scanner(new File("Locations.txt"));
				scanner.nextLine();
				scanner.nextLine();
				loc = scanner.nextLine();
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}

		}else{
			loc = WebController.user.getLocation();
		}
		UserTestApplication.restart();
		System.out.println(loc);
		model.put("location", loc);
		return new ModelAndView("map.html");
	}

	@RequestMapping(value = "/report.html", method = RequestMethod.GET)
	public String report(Model model){
		model.addAttribute("location", new Location());
		return "report";
	}






}
