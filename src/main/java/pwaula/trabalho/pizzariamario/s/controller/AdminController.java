package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.dto.*;
import pwaula.trabalho.pizzariamario.s.model.*;
import pwaula.trabalho.pizzariamario.s.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductInCartRepository productInCartRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private LocalOrderRepository localOrderRepository;

    @Autowired
    private LocalProductOrderedRepository localProductOrderedRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("")
    public ModelAndView controlPanel() {
        return new ModelAndView("controlpanel");
    }

    @GetMapping("/produtos")
    public ModelAndView controlPanelProducts() {
        ModelAndView mv = new ModelAndView("productsPanel");
        mv.addObject("products", productRepository.findAll());
        return mv;
    }

    @GetMapping("/produtos/criarProduto")
    public ModelAndView createNewProduct() {
        ModelAndView mv = new ModelAndView("createProduct");
        mv.addObject("productDTO", new ProductDTO());
        return mv;
    }

    @PostMapping("/produtos/criarProduto")
    public ModelAndView createNewProductPost(@ModelAttribute ProductDTO productDTO) {
        ProductEntity product = new ProductEntity(productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getImageUrl(),
                ProductCategory.valueOf(productDTO.getCategory()),
                productDTO.isAvailableForProduction());
        productRepository.save(product);
        ModelAndView mv = new ModelAndView("redirect:/admin/produtos");
        return mv;
    }

    @GetMapping("/produtos/editarProduto/{productId}")
    public ModelAndView editProduct(@PathVariable String productId) {
        ProductEntity product = productRepository.findById(productId).get();

        ModelAndView mv = new ModelAndView("updateProduct");
        mv.addObject("product", product);
        return mv;
    }

    @PutMapping("/produtos/editarProduto/{productId}")
    public ModelAndView editProductPut(@PathVariable String productId, @ModelAttribute ProductDTO productDTO) {
        ProductEntity product = productRepository.findById(productId).get();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setProductCategory(ProductCategory.valueOf(productDTO.getCategory()));
        product.setAvailableForProduction(productDTO.isAvailableForProduction());

        productRepository.save(product);
        ModelAndView mv = new ModelAndView("redirect:/admin/produtos");
        return mv;
    }

    @DeleteMapping("/produtos/deletarProduto/{productId}")
    public ModelAndView deleteProduct(@PathVariable String productId) {
        ProductEntity product = productRepository.findById(productId).get();
        productRepository.delete(product);
        return new ModelAndView("redirect:/admin/produtos");
    }

    @GetMapping("/usuarios")
    public ModelAndView controlPanelUsers() {
        ModelAndView mv = new ModelAndView("usersPanel");

        List<UserEntity> clientes = userRepository.findAll().stream().filter(userEntity -> userEntity.getRoles().equals("USER")).toList();
        List<UserEntity> admins = userRepository.findAll().stream().filter(userEntity -> userEntity.getRoles().equals("ADMIN")).toList();
        List<UserEntity> atendentes = userRepository.findAll().stream().filter(userEntity -> userEntity.getRoles().equals("ATENDENTE")).toList();

        List<ClientDTO> listOfClientDTOs = new ArrayList<>(clientes.stream().map(clientEntity -> new ClientDTO(
                clientEntity.getId(),
                clientRepository.findClientEntityByUserId(clientEntity.getId()).getName(),
                clientRepository.findClientEntityByUserId(clientEntity.getId()).getEmail(),
                clientRepository.findClientEntityByUserId(clientEntity.getId()).getPhone(),
                clientRepository.findClientEntityByUserId(clientEntity.getId()).getAddress(),
                clientRepository.findClientEntityByUserId(clientEntity.getId()).getCpf()
        )).toList());

        mv.addObject("clientes", listOfClientDTOs);
        mv.addObject("admins", admins);
        mv.addObject("atendentes", atendentes);
        return mv;
    }

    @GetMapping("/usuarios/adicionarAtendente")
    public ModelAndView controlPanelUsersAddAttendant() {
        ModelAndView mv = new ModelAndView("createAttendant");
        mv.addObject("userDTO", new UserDTO());
        return mv;
    }

    @PostMapping("/usuarios/adicionarAtendente")
    public ModelAndView controlPanelUsersAddAttendantPost(@ModelAttribute UserDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles("ATENDENTE");
        userRepository.save(user);

        return new ModelAndView("redirect:/admin/usuarios");
    }

    @DeleteMapping("/usuarios/deletarAtendente/{attendantId}")
    public ModelAndView controlPanelUsersDeleteAttendant(@PathVariable String attendantId) {
        UserEntity user = userRepository.findById(attendantId).get();
        userRepository.delete(user);
        return new ModelAndView("redirect:/admin/usuarios");
    }

    @GetMapping("/usuarios/atualizarAtendente/{attendantId}")
    public ModelAndView controlPanelUsersUpdateAttendant(@PathVariable String attendantId) {
        ModelAndView mv = new ModelAndView("updateAttendant");
        UserEntity atendente = userRepository.findById(attendantId).get();
        UserDTO atendenteDTO = new UserDTO();
        atendenteDTO.setId(atendente.getId());
        atendenteDTO.setEmail(atendente.getEmail());
        mv.addObject("atendente", atendenteDTO);
        return mv;
    }

    @PutMapping("/usuarios/atualizarAtendente/{attendantId}")
    public ModelAndView controlPanelUsersAddAttendantPut(@PathVariable String attendantId, @ModelAttribute UserDTO atendenteDTO) {
        UserEntity atendente = userRepository.findById(attendantId).get();
        atendente.setEmail(atendenteDTO.getEmail());
        atendente.setPassword(passwordEncoder.encode(atendenteDTO.getPassword()));
        atendente.setRoles("ATENDENTE");
        userRepository.save(atendente);

        return new ModelAndView("redirect:/admin/usuarios");
    }

    @DeleteMapping("/usuarios/deletarCliente/{clientId}")
    public ModelAndView controlPanelUsersDeleteClient(@PathVariable String clientId) {
        UserEntity user = userRepository.findById(clientId).get();
        userRepository.delete(user);
        return new ModelAndView("redirect:/admin/usuarios");
    }

    @GetMapping("/usuarios/atualizarCliente/{clientId}")
    public ModelAndView controlPanelUsersUpdateClient(@PathVariable String clientId) {
        ModelAndView mv = new ModelAndView("updateClient");
        ClientEntity cliente = clientRepository.findClientEntityByUserId(clientId);
        ClientUpdateDTO clientUpdateDTO = new ClientUpdateDTO();
        clientUpdateDTO.setUserId(clientId);
        clientUpdateDTO.setPhone(cliente.getPhone());
        clientUpdateDTO.setEmail(cliente.getEmail());
        clientUpdateDTO.setAddress(cliente.getAddress());
        clientUpdateDTO.setCpf(cliente.getCpf());
        clientUpdateDTO.setName(cliente.getName());
        mv.addObject("cliente", clientUpdateDTO);
        return mv;
    }

    @PutMapping("/usuarios/atualizarCliente/{clientId}")
    public ModelAndView controlPanelUsersAddClientPut(@PathVariable String clientId, @ModelAttribute ClientUpdateDTO clientUpdateDTO) {
        UserEntity clienteUserEntity = userRepository.findById(clientId).get();
        ClientEntity cliente = clientRepository.findClientEntityByUserId(clientId);

        System.out.println(clienteUserEntity.getEmail());
        System.out.println(cliente.getName());

        cliente.setPhone(clientUpdateDTO.getPhone());
        cliente.setEmail(clientUpdateDTO.getEmail());
        cliente.setAddress(clientUpdateDTO.getAddress());
        cliente.setCpf(clientUpdateDTO.getCpf());
        cliente.setName(clientUpdateDTO.getName());
        clienteUserEntity.setEmail(clientUpdateDTO.getEmail());
        clienteUserEntity.setPassword(passwordEncoder.encode(clientUpdateDTO.getPassword()));

        userRepository.save(clienteUserEntity);
        clientRepository.save(cliente);

        return new ModelAndView("redirect:/admin/usuarios");
    }




}
