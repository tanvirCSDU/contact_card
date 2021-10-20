package com.example.handlingformsubmission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



public class Greeting {


	private String name;
	private String phone_no;
	private String email;
	private String address;
	private String note;
	private String category;


	public Greeting() {
	}

	public Greeting(String name, String phone_no, String email, String address, String note) {
		this.name = name;
		this.phone_no = phone_no;
		this.email = email;
		this.address = address;
		this.note = note;
	}

	public Greeting(String name, String phone_no, String email, String address, String category, String note) {
		this.name = name;
		this.phone_no = phone_no;
		this.email = email;
		this.address = address;
		this.note = note;
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	@Override
	public String toString() {
		return
				"name='" + name + '\'' +
				", phone_no='" + phone_no + '\'' +
				", email='" + email + '\'' +
				", address='" + address + '\'' +
				", note='" + note + '\'' +
				", category='" + category + '\''
				;
	}

	@Controller
	public static class GreetingController {



		@Autowired
		private JdbcTemplate jdbcTemplate;


		@GetMapping("/greeting")
		public String greetingForm(Model model) {
			model.addAttribute("greeting", new Greeting());
			return "greeting";
		}

		@PostMapping("/greeting")
		public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {


			System.out.println(greeting.getName());
			System.out.println(greeting.getAddress());
			System.out.println(greeting.getPhone_no());
			System.out.println(greeting.getEmail());
			System.out.println(greeting.category);

			model.addAttribute("greeting", greeting);


			jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS contacts(name VARCHAR(100) , phone_no VARCHAR(100), email VARCHAR (100), address VARCHAR (500), category VARCHAR (100), note VARCHAR (500))");



			jdbcTemplate.update("INSERT INTO contacts (name,phone_no,email,address,category,note) VALUES (? , ?, ?, ?, ?,?)", new Object[]{greeting.getName(),greeting.getPhone_no(), greeting.getEmail(),greeting.getAddress(),greeting.getCategory(),greeting.getNote()});


//			List<Greeting> specific = jdbcTemplate.query("SELECT * FROM contacts WHERE name=? ",new Object[]{greeting.getName()},
//					(resultSet, rowNum) -> new Greeting(resultSet.getString("name"), resultSet.getString("phone_no"), resultSet.getString( "email"), resultSet.getString("address"),resultSet.getString("category"),resultSet.getString("note")));
////

//			specific.forEach(System.out::println);

			//"INSERT INTO Test (ID, NAME) VALUES (?, ?)",
			return "result";
		}



		@GetMapping("/norml")
			public String greetingShow(Model model) {


			List<Greeting> contacts = jdbcTemplate.query("SELECT * FROM contacts",
					(resultSet, rowNum) -> new Greeting(resultSet.getString("name"), resultSet.getString("phone_no"), resultSet.getString("email"), resultSet.getString("address"), resultSet.getString("category"), resultSet.getString("note")));

			contacts.forEach(System.out::println);
			//Print read records:
			model.addAttribute("contacts", contacts);
			return "norml";
		}


		@GetMapping("/family")
		public String familyShow(Model model) {

			List<Greeting> family = jdbcTemplate.query("SELECT * FROM contacts WHERE category='family' ",
					(resultSet, rowNum) -> new Greeting(resultSet.getString("name"), resultSet.getString("phone_no"), resultSet.getString( "email"), resultSet.getString("address"),resultSet.getString("category"),resultSet.getString("note")));

			family.forEach(System.out::println);
			model.addAttribute("family", family);
			return "family";

			}

		@GetMapping("/friends")
		public String friendsShow(Model model) {

			List<Greeting> friends = jdbcTemplate.query("SELECT * FROM contacts WHERE category='friend' ",
					(resultSet, rowNum) -> new Greeting(resultSet.getString("name"), resultSet.getString("phone_no"), resultSet.getString( "email"), resultSet.getString("address"),resultSet.getString("category"),resultSet.getString("note")));

			friends.forEach(System.out::println);
			model.addAttribute("friends", friends);
			return "friends";

		}

		@GetMapping("/work")
		public String wrokShow(Model model) {


			List<Greeting> work = jdbcTemplate.query("SELECT * FROM contacts WHERE category='work' ",
					(resultSet, rowNum) -> new Greeting(resultSet.getString("name"), resultSet.getString("phone_no"), resultSet.getString( "email"), resultSet.getString("address"),resultSet.getString("category"),resultSet.getString("note")));

			work.forEach(System.out::println);
			model.addAttribute("works", work);




			return "work";

		}

		@GetMapping("/search")
		public String searchForm(Model model) {

			model.addAttribute("search", new Greeting());
			return "search";
		}

		@PostMapping ("/search")
		public String searchEntity(@ModelAttribute Greeting greeting, Model model) {
//			System.out.println(greeting.getName());
//			System.out.println(greeting.getAddress());
//			System.out.println(greeting.getPhone_no());
//			System.out.println(greeting.getEmail());
//			System.out.println(greeting.category);

			//model.addAttribute("search", greeting);

			List<Greeting> searchRes = jdbcTemplate.query("SELECT * FROM contacts WHERE name=? ",new Object[]{greeting.getName()},
					(resultSet, rowNum) -> new Greeting(resultSet.getString("name"), resultSet.getString("phone_no"), resultSet.getString( "email"), resultSet.getString("address"),resultSet.getString("category"),resultSet.getString("note")));
//
			if(searchRes.isEmpty())
			{

				searchRes.add(new Greeting("Not Found", "Not Found", "Not Found", "Not Found", "Not Found","Not Found"));
			}

			searchRes.forEach(System.out::println);
			model.addAttribute("searchRes", searchRes);

			return "searchRes";

			//return "searchRes" ;
		}


		@GetMapping("/delete")
		public String delForm(Model model) {

			model.addAttribute("delete", new Greeting());
			return "delete";
		}

		@PostMapping ("/delete")
		public String delEntity(@ModelAttribute Greeting greeting, Model model) {
//			System.out.println(greeting.getName());
//			System.out.println(greeting.getAddress());
//			System.out.println(greeting.getPhone_no());
//			System.out.println(greeting.getEmail());
//			System.out.println(greeting.category);

			//model.addAttribute("search", greeting);

			 jdbcTemplate.update("DELETE FROM contacts WHERE name=? and phone_no=? and email=?",new Object[]{greeting.getName(), greeting.getPhone_no(),greeting.getEmail()});

//

			model.addAttribute("delete", greeting);

			return "deleteRes";

			//return "searchRes" ;
		}





	}
}
