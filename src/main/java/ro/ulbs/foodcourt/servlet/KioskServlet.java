// Interfața clienților pentru meniu

package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ro.ulbs.foodcourt.ejb.MenuBean;

import java.io.IOException;

@WebServlet(name = "KioskServlet", value = "/kiosk")
public class KioskServlet extends HttpServlet {

    @EJB
    private MenuBean menuBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Preia produsele din baza de date și le trimite către pagină
        request.setAttribute("categories", menuBean.getAllCategories());
        request.setAttribute("menuItems", menuBean.getAllMenuItems());

        request.getRequestDispatcher("/kiosk.jsp").forward(request, response);
    }
}