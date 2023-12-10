package application;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Department obj = new Department(1,"Books");
		System.out.println(obj);
		
		Seller seller = new Seller
			(10,"Peter Brown", 
			"pete@gmail.com",
			new Date(),2000.0, obj);
	
		System.out.println(seller);
		
	}

}
