# SpringMVC-HiringPlatformWebApp

Topics Covered:-
Spring Web MVC:

Data binding with
    1. @RequestParam
    2. @PathVariable
    3. @ModelAttribute

Data Rendering
    1. With Thymeleaf
    2. Data transfer from controller to view

Annotations:
    1. @Controller
    2. @RequestMapping
    3. @GetMapping
    4. @PostMapping

 Working with:
    1. HttpRequest, HttpResponse, HttpSession
    2. Cookies and @CookieValue
    3. Redirects in Spring MVC, RedirectAttributes, addAttribute(), addFlashAttribute()

Configurations (application.properties):
    1. setting context path using "server.servlet.context-path" property
    2. changing default dispatcher servlet mapping using "spring.mvc.servlet.path" property
    3. View resolver configurations (For both Thymeleaf & Jsp(in comments only, since jsp is not used in this project))

Interceptors (pre & post request processing) (for checking whether session is expired, if yes then the user is redirected to login page)
@ControllerAdvice and @ExceptionHandler


Topics not covered:-
Spring MVC Tag Library (not covered since we are using Thymeleaf for our project.)




Spring Data Jpa:
Topics Covered:-
1. Inheritance Mapping
2. Association Mapping (One to One, One to Many, Many to One)
3. Collection Mapping

