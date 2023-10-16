package br.com.esdras.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
    //vai pegar a "funcao" getNullPropertyNames sera atribuido para o BeanUtils, para se mesclar as informacoes
    public static void copyNonNullProperties(Object source, Object target)
    {
        //se copia as propriedade de valores de um objeto para outro objeto
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
    //getNullPropertyNames: vai servir para fazer uma copia do objeto do repositorio para o objeto body em TaskController
    public static String[] getNullPropertyNames(Object source){
        //beanwrapper: classe do java, interface que permite o acesso de propriedades de um objeto dentro do java
        //BeanWrapperImpl: impantacao da interface
        final BeanWrapper src = new BeanWrapperImpl(source);
        
        //obtendo as propriedades do objeto
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        //propriedades de valores nulos
        Set<String> emptyNames= new HashSet<>();


        for(PropertyDescriptor pd: pds){
            //para cada property value: era ser pego o getName obtendo o valor da propriedade atual
            Object srcValue= src.getPropertyValue(pd.getName());
            //verificando se e nula, e se for vai ser adicionada  o nome da prorpriedade
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        //array de string para armazenar o nome de todas as propriedades
        String [] result = new String[emptyNames.size()];
        // sera convertido o conjunto de nome de propriedades para um array de strings
        return emptyNames.toArray(result);


    }
}
