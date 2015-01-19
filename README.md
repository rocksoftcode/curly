curly
=====

A Groovy wrapper for cURL (http://curl.haxx.se/).  Requires that cURL be installed, which will automatically be the case if you have a Mac or Linux machine.

This library is particularly useful if you want to make a quick and easy call to a URL to retrieve the body, or some basic information like the content type or status code.  This can be particularly useful if you want to circumvent Java's finnicky way of dealing with SSL certificates. 

*Usage:*
```
String lotsOfHtml = Curly.forText("http://www.detroitlions.com")
Integer fourOhFour = Curly.forStatus("http://www.detroitlions.com/superbowls")
CurlResponse response = Curly.forResponse("http://www.deathsdoorspirits.com", CurlField.NUMBER_OF_REDIRECTS, CurlField.TIME_TOTAL)
assert response.fields[CurlField.NUMBER_OF_REDIRECTS].toInteger() == 2
assert response.fields[CurlField.TIME_TOTAL].toInteger() < 60
```
Hopefully you find this library useful.  

*TODO:* The next logical enhancement would be to add request body. 
