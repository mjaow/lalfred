package org.loda.lalfred;

public class DemoSearcher implements ISearcher{

	@Override
	public void search() {
		System.out.println("search");
	}

	@Override
	public void sayName(String name) {
		System.out.println("say "+name);
	}

}
