/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.transferobject.reference;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jasig.ssp.model.reference.Color;
import org.jasig.ssp.transferobject.TransferObject;
import org.jasig.ssp.model.ObjectStatus;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.collect.Lists;

/**
 *Color transfer object
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorTO
		extends AbstractReferenceTO<Color>
		implements TransferObject<Color> { 

	private String code;
	private String hexCode;

	/**
	 * Empty constructor
	 */
	public ColorTO() {
		super();
	}
	
	public ColorTO(final Color model) {
		super();
		from(model);
	}

	public ColorTO(final UUID id, final String name,
			final String description) {
		super(id, name, description);
	}

	

	@Override
	public final void from(final Color model) {
		/*if (model == null) {
			throw new IllegalArgumentException("Model can not be null.");
		}*/

		super.from(model);
		code = model.getCode();
		hexCode = model.getHexCode();
		
	}
	
	public static List<ColorTO> toTOList(
			final Collection<Color> models) {
		final List<ColorTO> tObjects = Lists.newArrayList();
		for (final Color model : models) {
			tObjects.add(new ColorTO(model)); 
		}

		return tObjects;
	}
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}

	public String getHexCode() {
		return hexCode;
	}

	public void setHexCode(String hexCode) {
		this.hexCode = hexCode;
	}	
}