package com.example.ooplab3;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/FeedBack")
public class FeedBack extends HttpServlet {
    private static final String filePath = "students.json";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String lastname = request.getParameter("lastname");
        int age = Integer.parseInt(request.getParameter("age"));
        String email = request.getParameter("email");
        String telegram = request.getParameter("telegram");
        String mobile = request.getParameter("mobile");

        JSONObject feedback = new JSONObject();
        feedback.put("name", name);
        feedback.put("lastname", lastname);
        feedback.put("age", age);
        feedback.put("email", email);
        feedback.put("telegram", telegram);
        feedback.put("mobile", mobile);

        JSONArray feedbackList = new JSONArray();

        try {
            JSONParser parser = new JSONParser();
            File file = new File(filePath);
            String fullPath = file.getAbsolutePath();
            System.out.println(fullPath);
            if (file.exists()) {
                feedbackList = (JSONArray) parser.parse(new FileReader(filePath));
            }
            feedbackList.add(feedback);
            System.out.println("Feedback List: " + feedbackList);
            FileWriter fileWriter = new FileWriter(filePath);

            fileWriter.write(feedbackList.toJSONString());
            fileWriter.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        response.sendRedirect("lab3_war_exploded");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONParser parser = new JSONParser();
            JSONArray feedbackList = (JSONArray) parser.parse(new FileReader(filePath));

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Запись</title><link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"></head><body style=\"background-color: #0a58ca\"><div><table class=\"table\"><thead><tr><th scope=\"col\">Имя</th><th scope=\"col\">Фамилия</th><th scope=\"col\">Возраст</th><th scope=\"col\">Телеграм</th><th scope=\"col\">Почта</th><th scope=\"col\">Телефон</th></tr></thead>");
            for (Object obj : feedbackList) {
                JSONObject feedback = (JSONObject) obj;
                out.println("<tbody><tr><td>" + feedback.get("name") + "</td><td>" + feedback.get("lastname") + "</td><td>" + feedback.get("age") + "</td><td>" + feedback.get("email") + "</td><td>" + feedback.get("telegram") + "</td><td>" + feedback.get("mobile") + "</td></tr>");
            }
            out.println("</tbody></table></div><script src=\"js/bootstrap.bundle.min.js\"></script></body></html>");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}
