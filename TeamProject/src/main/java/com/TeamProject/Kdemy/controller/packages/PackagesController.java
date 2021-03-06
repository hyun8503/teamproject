package com.TeamProject.Kdemy.controller.packages;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.TeamProject.Kdemy.model.packages.dto.PackagesDTO;
import com.TeamProject.Kdemy.service.packages.PackagesService;
import com.TeamProject.Kdemy.util.UploadFileUtils;

@Controller
@RequestMapping("packages/*")
public class PackagesController {
   private static final Logger logerr=LoggerFactory.getLogger(PackagesController.class);

   @Inject
   PackagesService packagesService;

   @Resource(name="packagesuploadPath")
   String packagesuploadPath;

   @RequestMapping("list.do")
   public ModelAndView packagesList(ModelAndView mav) {
      List<PackagesDTO> list=packagesService.list();
      mav.setViewName("packages/packages_list");
      mav.addObject("list", list);
      return mav;
   }
   @RequestMapping("adminlist.do")
   public ModelAndView adminpackagesList(ModelAndView mav) {
      List<PackagesDTO> list=packagesService.list();
      mav.addObject("list",list);
      mav.setViewName("admin/packages_list");
      return mav;
   }
   @RequestMapping(value="insert.do",method= {RequestMethod.POST},
         consumes=MediaType.MULTIPART_FORM_DATA_VALUE,produces="text/plain;charset=utf-8")
   public String insertpackages(PackagesDTO dto, ModelAndView mav) throws Exception {
      MultipartFile file1 = dto.getfile1();
      String packages_image = file1.getOriginalFilename();
      try {
         packages_image = UploadFileUtils.uploadFile(packagesuploadPath,packages_image, file1.getBytes());
      } catch (Exception e) {
         e.printStackTrace();
      }
      dto.setPackages_image(packages_image);
      packagesService.insertpackages(dto);
      return ("redirect:/packages/adminlist.do");
   }
   @RequestMapping(value="/packages_view.do",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
   public ModelAndView view(String packages_name,ModelAndView mav) {
         PackagesDTO dto=packagesService.viewPackages(packages_name);
         mav.addObject("dto",dto);
         mav.setViewName("admin/packages_view");
         return mav;
      }
   @RequestMapping(value="packagesUpdate.do",method= {RequestMethod.POST},
         consumes=MediaType.MULTIPART_FORM_DATA_VALUE,produces="text/plain;charset=utf-8")
   public String update(PackagesDTO dto) {
      MultipartFile file1 = dto.getfile1();
      String packages_image = file1.getOriginalFilename();
      System.out.println("packages_image:"+packages_image);
      if(packages_image != "") {
         try {
            packages_image = UploadFileUtils.uploadFile(packagesuploadPath,packages_image, file1.getBytes());
            dto.setPackages_image(packages_image);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }else {
         packages_image = dto.getPackages_image();
         dto.setPackages_image(packages_image);
      }
      packagesService.updatePackages(dto);
      return "redirect:/packages/adminlist.do";
   }
   @RequestMapping("deletepackages.do")
   public String delete(@RequestParam String packages_name,ModelAndView mav) {
         packagesService.deletePackages(packages_name);
         return ("redirect:/packages/adminlist.do");
   }
}