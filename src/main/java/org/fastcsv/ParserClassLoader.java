package org.fastcsv;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ACC_BRIDGE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ARRAYLENGTH;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.IF_ICMPLE;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_5;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.fastcsv.annotation.FieldConverter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ParserClassLoader extends ClassLoader{

	private static ParserClassLoader instance;
	
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> classMap = new ConcurrentHashMap<String, Class>();
	
	private ParserClassLoader(){
	}
	
	public static ParserClassLoader getInstance(){
		if(instance == null){
			instance = new ParserClassLoader();
		}
		return instance;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Parser<T> getParser(Class<T> model) {
		String modelInternalName = Type.getInternalName(model);
		
		Class clazz = classMap.get(modelInternalName);
		
		if(clazz == null){
			String parserClass = "org/fastcsv/dynamic/" + model.getSimpleName() + "ParserImpl";
			String sig = "Ljava/lang/Object;L" + Type.getInternalName(Parser.class) + "<L" + modelInternalName + ";>;";
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cw.visit(V1_5, 
					ACC_PUBLIC + ACC_SUPER, 
					parserClass, 
					sig,
			   Type.getInternalName(Object.class),
			   new String[] { Type.getInternalName(Parser.class)});
			 
			MethodVisitor consMv = cw.visitMethod(ACC_PUBLIC, "<init>","()V",null,null);
			consMv.visitCode();
			consMv.visitVarInsn(ALOAD, 0);
			consMv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V");
			consMv.visitInsn(RETURN);
			consMv.visitMaxs(1, 1);
			consMv.visitEnd();
			 
			MethodVisitor runMv = cw.visitMethod(
					ACC_PUBLIC, 
					"parse", 
					"(Ljava/util/Map;[Ljava/lang/String;)L" + modelInternalName + ";", 
					"(Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;[Ljava/lang/String;)L" + modelInternalName + ";", 
					null);
			runMv.visitCode();
			Label l0 = new Label();
			runMv.visitLabel(l0);
			runMv.visitTypeInsn(NEW, modelInternalName);
			runMv.visitInsn(DUP);
			runMv.visitMethodInsn(INVOKESPECIAL, modelInternalName, "<init>", "()V");
			runMv.visitVarInsn(ASTORE, 3);
			Label l1 = new Label();
			runMv.visitLabel(l1);
			
			Field[] fields = model.getDeclaredFields();
					
			for (Field field : fields) {
				org.fastcsv.annotation.Field mapToField = field.getAnnotation(org.fastcsv.annotation.Field.class);
				if(mapToField == null)
					continue;
				
				runMv.visitVarInsn(ALOAD, 1);
				runMv.visitLdcInsn(mapToField.value());
				runMv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
				runMv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
				runMv.visitVarInsn(ASTORE, 4);
				Label l2 = new Label();
				runMv.visitLabel(l2);
				runMv.visitVarInsn(ALOAD, 4);
				Label l3 = new Label();
				runMv.visitJumpInsn(IFNULL, l3);
				Label columnExistTest = new Label();
				runMv.visitLabel(columnExistTest);
				runMv.visitVarInsn(ALOAD, 2);
				runMv.visitInsn(ARRAYLENGTH);
				runMv.visitVarInsn(ALOAD, 4);
				runMv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
				runMv.visitJumpInsn(IF_ICMPLE, l3);
				
				FieldConverter converter = field.getAnnotation(FieldConverter.class);
				String fieldSetter = "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
				if(converter != null){
					Label l4 = new Label();
					runMv.visitLabel(l4);
					runMv.visitTypeInsn(NEW, Type.getInternalName(converter.value()));
					runMv.visitInsn(DUP);
					runMv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(converter.value()), "<init>", "()V");
					runMv.visitVarInsn(ASTORE, 5);
					Label l5 = new Label();
					runMv.visitLabel(l5);
					runMv.visitVarInsn(ALOAD, 3);
					runMv.visitVarInsn(ALOAD, 5);
					runMv.visitVarInsn(ALOAD, 2);
					runMv.visitVarInsn(ALOAD, 4);
					runMv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
					runMv.visitInsn(AALOAD);
					runMv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(Converter.class), "convert", "(Ljava/lang/String;)Ljava/lang/Object;");
					runMv.visitTypeInsn(CHECKCAST, Type.getInternalName(field.getType()));
					runMv.visitMethodInsn(INVOKEVIRTUAL, modelInternalName, fieldSetter, "(L" + Type.getInternalName(field.getType()) + ";)V");
					//runMv.visitFieldInsn(PUTFIELD, modelInternalName, field.getName(), "L"+ Type.getInternalName(field.getType()) + ";");
					runMv.visitLabel(l3);
				}else{
					Label l4 = new Label();
					runMv.visitLabel(l4);
					runMv.visitVarInsn(ALOAD, 3);
					runMv.visitVarInsn(ALOAD, 2);
					runMv.visitVarInsn(ALOAD, 4);
					runMv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
					runMv.visitInsn(AALOAD);
					runMv.visitMethodInsn(INVOKEVIRTUAL, modelInternalName, fieldSetter, "(L" + Type.getInternalName(field.getType()) + ";)V");
					//runMv.visitFieldInsn(PUTFIELD, modelInternalName, field.getName(), "L" + Type.getInternalName(field.getType()) + ";");
					runMv.visitLabel(l3);
				}
			}
			runMv.visitFrame(Opcodes.F_APPEND,2, new Object[] {modelInternalName, "java/lang/Integer"}, 0, null);
			runMv.visitVarInsn(ALOAD, 3);
			runMv.visitInsn(ARETURN);	
			runMv.visitMaxs(3, 5);
			runMv.visitEnd();
			
			MethodVisitor interfaceVisitor = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "parse", "(Ljava/util/Map;[Ljava/lang/String;)Ljava/lang/Object;", null, null);
			interfaceVisitor.visitCode();
			Label l00 = new Label();
			interfaceVisitor.visitLabel(l00);
			interfaceVisitor.visitLineNumber(1, l00);
			interfaceVisitor.visitVarInsn(ALOAD, 0);
			interfaceVisitor.visitVarInsn(ALOAD, 1);
			interfaceVisitor.visitVarInsn(ALOAD, 2);
			interfaceVisitor.visitMethodInsn(INVOKEVIRTUAL, parserClass, "parse", "(Ljava/util/Map;[Ljava/lang/String;)L" + modelInternalName + ";");
			interfaceVisitor.visitInsn(ARETURN);
			interfaceVisitor.visitMaxs(3, 3);
			interfaceVisitor.visitEnd();
			
			byte[] bytes = cw.toByteArray();
			
			clazz = defineClass(parserClass.replace('/', '.'), bytes, 0, bytes.length);
			classMap.put(parserClass, clazz);
		}
		
		try {
			return (Parser<T>) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new ParserException("Unable to create parser", e);
		} catch (IllegalAccessException e) {
			throw new ParserException("Unable to create parser", e);
		}
	}
	
}
