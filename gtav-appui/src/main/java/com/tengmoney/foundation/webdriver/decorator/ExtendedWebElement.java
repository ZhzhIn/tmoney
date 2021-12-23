/*
 * TODO 装饰器模式
 */
package com.tengmoney.foundation.webdriver.decorator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtendedWebElement
{
	private WebElement element;
	private String name;
	private By by;

	public ExtendedWebElement(WebElement element)
	{
		this.element = element;
	}

	public ExtendedWebElement(WebElement element, String name)
	{
		this.element = element;
		this.name = name;
	}
	
	public ExtendedWebElement(WebElement element, String name, By by)
	{
		this.element = element;
		this.name = name;
		this.by = by;
	}

	public WebElement getElement()
	{
		return element;
	}

	public void setElement(WebElement element)
	{
		this.element = element;
	}

	public String getName()
	{
		return name;
	}
	
	public String getNameWithLocator()
	{
		return by != null ? name + String.format(" (%s)", by) : name + " (n/a)";
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getText()
	{
		return element != null ? element.getText() : null;
	}

	public String getAttribute(String attributeName)
	{
		return element != null ? element.getAttribute(attributeName) : null;
	}
	
	public By getBy()
	{
		return by;
	}

	public void setBy(By by)
	{
		this.by = by;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
