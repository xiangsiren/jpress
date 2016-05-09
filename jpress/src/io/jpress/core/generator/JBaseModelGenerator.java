/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.core.generator;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.TableMeta;

public class JBaseModelGenerator extends BaseModelGenerator {

	public JBaseModelGenerator(String baseModelPackageName,
			String baseModelOutputDir) {
		super(baseModelPackageName, baseModelOutputDir);
		
		this.packageTemplate = "/**%n"
				+ " * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).%n"
				+ " *%n"
				+ " * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the \"License\");%n"
				+ " * you may not use this file except in compliance with the License.%n"
				+ " * You may obtain a copy of the License at%n"
				+ " *%n"
				+ " *      http://www.gnu.org/licenses/lgpl-3.0.txt%n"
				+ " *%n"
				+ " * Unless required by applicable law or agreed to in writing, software%n"
				+ " * distributed under the License is distributed on an \"AS IS\" BASIS,%n"
				+ " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.%n"
				+ " * See the License for the specific language governing permissions and%n"
				+ " * limitations under the License.%n"
				+ " */%n"
				+"package %s;%n%n";

		this.classDefineTemplate = "/**%n"
				+ " *  Auto generated by JPress, do not modify this file.%n"
				+ " */%n"
				+ "@SuppressWarnings(\"serial\")%n"
				+ "public abstract class %s<M extends %s<M>> extends JModel<M> implements IBean {%n%n"
				+ "\tpublic static final String CACHE_NAME = \"%s\";%n"
				+ "\tpublic static final String METADATA_TYPE = \"%s\";%n%n"
				
				+ "\tpublic void removeCache(Object key){%n"
				+ "\t\tCacheKit.remove(CACHE_NAME, key);%n"
				+ "\t}%n%n"
				
				
				+ "\tpublic void putCache(Object key,Object value){%n"
				+ "\t\tCacheKit.put(CACHE_NAME, key, value);%n"
				+ "\t}%n%n"
				
				
				+ "\tpublic M getCache(Object key){%n"
				+ "\t\treturn CacheKit.get(CACHE_NAME, key);%n"
				+ "\t}%n%n"
				
				
				+ "\tpublic M getCache(Object key,IDataLoader dataloader){%n"
				+ "\t\treturn CacheKit.get(CACHE_NAME, key, dataloader);%n"
				+ "\t}%n%n"
				
				
				+ "\tpublic Metadata findMetadata(String key){%n"
				+ "\t\treturn Metadata.findByTypeAndIdAndKey(METADATA_TYPE, getId(), key);%n"
				+ "\t}%n%n"
				

				+ "\tpublic List<Metadata> findMetadataList(){%n"
				+ "\t\treturn Metadata.findListByTypeAndId(METADATA_TYPE, getId());%n"
				+ "\t}%n%n"
				
				
				
				+ "\tpublic M findFirstFromMetadata(String key,Object value){%n"
				+ "\t\tMetadata md = Metadata.findFirstByTypeAndValue(METADATA_TYPE, key, value);%n"
				+ "\t\tif(md != null){%n"
				+ "\t\t\tBigInteger id = md.getObjectId();%n"
				+ "\t\t\treturn findById(id);%n"
				+ "\t\t}%n"
				+ "\t\treturn null;%n"
				+ "\t}%n%n"
				
		
				+ "\tpublic Metadata createMetadata(){%n"
				+ "\t\tMetadata md = new Metadata();%n"
				+ "\t\tmd.setObjectId(getId());%n"
				+ "\t\tmd.setObjectType(METADATA_TYPE);%n"
				+ "\t\treturn md;%n"
				+ "\t}%n%n"
				
				
				+ "\tpublic Metadata createMetadata(String key,String value){%n"
				+ "\t\tMetadata md = new Metadata();%n"
				+ "\t\tmd.setObjectId(getId());%n"
				+ "\t\tmd.setObjectType(METADATA_TYPE);%n"
				+ "\t\tmd.setMetaKey(key);%n"
				+ "\t\tmd.setMetaValue(value);%n"
				+ "\t\treturn md;%n"
				+ "\t}%n%n";

		

		
		this.importTemplate = "import io.jpress.core.JModel;%n"
				+ "import io.jpress.model.Metadata;%n%n"
				+ "import java.util.List;%n"
				+ "import java.math.BigInteger;%n%n"
				+ "import com.jfinal.plugin.activerecord.IBean;%n"
				+ "import com.jfinal.plugin.ehcache.CacheKit;%n"
				+ "import com.jfinal.plugin.ehcache.IDataLoader;%n%n";
		

	}

	@Override
	protected void genClassDefine(TableMeta tableMeta, StringBuilder ret) {
		ret.append(String.format(classDefineTemplate, tableMeta.baseModelName,
				tableMeta.baseModelName, tableMeta.name, tableMeta.name));
	}
	
	protected String idGetterTemplate =
			"\tpublic java.math.BigInteger getId() {%n" +
				"\t\tObject id = get(\"id\");%n" +
				"\t\tif (id == null)%n" +
				"\t\t\treturn null;%n%n" +
				"\t\treturn id instanceof BigInteger ? (BigInteger)id : new BigInteger(id.toString());%n" +
			"\t}%n%n";
	
	
	@Override
	protected void genGetMethodName(ColumnMeta columnMeta, StringBuilder ret) {
		if("id".equals(columnMeta.attrName)){
			ret.append(String.format(idGetterTemplate));
		}else{
			String getterMethodName = "get" + StrKit.firstCharToUpperCase(columnMeta.attrName);
			String getter = String.format(getterTemplate, columnMeta.javaType, getterMethodName, columnMeta.name);
			ret.append(getter);
		}
	}

}
