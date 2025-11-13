package IngS.tarea.controller;

import IngS.tarea.model.Usuario;
import IngS.tarea.repository.UsuarioRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private UsuarioRep usuarioRepository;

    @RequestMapping("/")
    public String index() {
        return "Hola desde spring";
    }

    //Create
    @PostMapping("/crear")
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    //Read
    @GetMapping("/usuarios")
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping(value = "/buscar/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ((usuarioRepository.existsById(id))? new ResponseEntity<>(usuarioRepository.findById(id), HttpStatus.OK) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario con id " + id + " no fue encontrado"));
    }

    //Update
    @PostMapping("/actualizar/{id}")
    public String updateUsuario (@PathVariable Long id, @RequestBody Usuario usuario) {
            if(usuarioRepository.existsById(id)){
                Usuario usuarioViejo = usuarioRepository.findById(id).get();
                if(usuario.getNumero() != null) usuarioViejo.setNumero(usuario.getNumero());
                if(usuario.getCorreo() != null) usuarioViejo.setCorreo(usuario.getCorreo());
                if(usuario.getDireccion() != null) usuarioViejo.setDireccion(usuario.getDireccion());
                usuarioRepository.save(usuarioViejo);
                return "El usuario " + id + " se ha actualizado correctamente los datos de Numero, Correo y Direccion (Los demas fueron ignorados)";
            }else{
                return "Usuario con id: " + id + " no fue encontrado";
            }
    }

    //Delete
    @GetMapping(value = "/borrar/{id}")
    public String deleteUsuario(@PathVariable Long id){
        try{
            usuarioRepository.deleteById(id);
            return "Usuario borrado";
        }catch(Exception e) {
            return "Error al borrar usuario Error:" + e.getMessage();
        }
    }

}
