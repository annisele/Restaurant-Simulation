package restaurant.test.mock;

public class Mock {
protected String name;
public Mock(String name){
	this.name=name;
}
public String getName(){
	return name;
	
}
public String toString(){
	return this.getClass().getName()+": "+name;
}
}
