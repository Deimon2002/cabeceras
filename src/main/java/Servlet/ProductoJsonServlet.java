package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import Service.ProductoService;
import Service.ProductoServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
// Anotación que indica que este servlet responde a la URL "/archivo.json"
@WebServlet("/archivo.json")
public class ProductoJsonServlet extends HttpServlet {

    // Método que se ejecuta cuando se realiza una petición HTTP GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Se crea una instancia del servicio de productos
        ProductoService service = new ProductoServiceImpl();
        // Se obtiene la lista de productos desde el servicio
        List<Producto> productos = service.listar();

        // Se establece el tipo de contenido de la respuesta como JSON
        resp.setContentType("application/json");
        // Se establece la codificación de caracteres a UTF-8
        resp.setCharacterEncoding("UTF-8");
        // Se indica que el contenido es un archivo adjunto llamado "archivo.json"
        resp.setHeader("Content-Disposition", "attachment; filename=archivo.json");

        // Se obtiene el escritor de la respuesta para enviar el contenido al cliente
        try (PrintWriter out = resp.getWriter()) {
            out.print("["); // Comienza el arreglo JSON

            // Se recorre la lista de productos
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i); // Se obtiene el producto actual

                out.print("{"); // Comienza el objeto JSON del producto

                // Se escriben las propiedades del producto en formato JSON
                out.print("\"id\":" + p.getId() + ",");
                out.print("\"nombre\":\"" + escapeJson(p.getNombre()) + "\",");
                out.print("\"tipo\":\"" + escapeJson(p.getTipo()) + "\",");
                out.print("\"precio\":" + p.getPrecio());

                out.print("}"); // Termina el objeto JSON del producto

                // Si no es el último producto, se agrega una coma para separar los objetos
                if (i < productos.size() - 1) {
                    out.print(",");
                }
            }

            out.print("]"); // Cierra el arreglo JSON
        }
    }

    // Método auxiliar que escapa caracteres especiales en cadenas JSON
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        // Reemplaza caracteres especiales por su forma escapada para evitar errores en el JSON
        return input.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}