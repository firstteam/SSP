/*
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
Ext.define('Ssp.service.CaseloadService', {  
    extend: 'Ssp.service.AbstractService',   		
    mixins: [ 'Deft.mixin.Injectable'],
    inject: {
    	apiProperties: 'apiProperties',
    	appEventsController: 'appEventsController'
    },
    initComponent: function() {
		return this.callParent( arguments );
    },
    
    getBaseUrl: function(){
		var me=this;
		var baseUrl = me.apiProperties.createUrl( me.apiProperties.getItemUrl('personCaseload') );
    	return baseUrl;
    },
    
    getBaseUrlForIdCaseload: function(id){
		var me=this;
		var baseUrl = me.apiProperties.createUrl( me.apiProperties.getItemUrl('personCaseloadId') );
		baseUrl = baseUrl.replace('{id}',id);
    	return baseUrl;
    },    
    getProgramAndStatusAgnosticCaseload: function( store, callbacks ){
    	var me=this;
	    
		// clear the store
		store.removeAll();

		Ext.apply(store.getProxy(),{url: me.getBaseUrl()});

	    store.load({
		    callback: function(records, operation, success) {
		        if (success)
		        {
			    	if (callbacks != null)
			    	{
			    		callbacks.success( records, callbacks.scope );
			    	}		        	
		        }else{
			    	if (callbacks != null)
			    	{
			    		callbacks.failure( records, callbacks.scope );
			    	}
		        }
		    },
		    scope: me
		});
    },
    getCaseloadCount: function( programStatusId, callbacks ){
    	var me=this;
	    
		var activeParams = {};
		
		// Set the Url for the Caseload Store
		// including param definitions because the params need
		// to be applied prior to load and not in a params 
		// definition from the load method or the paging
		// toolbar applied to the SearchView will not
		// apply the params when using next or previous
		// page
		var url;
		if(programStatusId)
		{
			activeParams['programStatusId'] = programStatusId;
		}
		
		activeParams['status'] = 'ACTIVE';
		
		var encodedUrl = Ext.urlEncode(activeParams);
		var count;

		 Ext.Ajax.request({
				url: me.getBaseUrl()+'/count?' + encodedUrl,
				method: 'GET',
				headers: { 'Content-Type': 'application/json' },
				success: function(response, view) {
					if (callbacks != null && callbacks.success ) {
						if ( response && response.responseText ) {
							callbacks.success.call(callbacks.scope, Ext.decode(response.responseText));
						} else {
							callbacks.success.call(callbacks.scope, null);
						}
					}
				},
				failure: function(response) {
					if (callbacks != null && callbacks.failure) {
						callbacks.failure.call(callbacks.scope, response);
					} else {
						this.apiProperties.handleError(response);
					}
				}
			}, this);
    },
    getCaseload: function( programStatusId, store, callbacks ){
    	var me=this;
	    
		store.removeAll();
		store.currentPage = 1;
		var activeParams = {};
		if(store.params){
			for (key in store.params) {
				if(store.params[key] && store.params[key] != null){
					activeParams[key] = store.params[key];
				}
			}
		}
		
		if(store.params){
			for (key in store.params) {
				if(store.params[key] && store.params[key] != null){
					activeParams[key] = store.params[key];
				}
			}
		}
		
		if(store.extraParams){
			for (key in store.extraParams) {
				if(store.extraParams[key] && store.extraParams[key] != null){
					activeParams[key] = store.extraParams[key];
				}
			}
		}
		
		// Set the Url for the Caseload Store
		// including param definitions because the params need
		// to be applied prior to load and not in a params 
		// definition from the load method or the paging
		// toolbar applied to the SearchView will not
		// apply the params when using next or previous
		// page
		var url;
		if(programStatusId)
		{
			activeParams['programStatusId'] = programStatusId;
		}
		
		activeParams['status'] = 'ACTIVE';
		
		var encodedUrl = Ext.urlEncode(activeParams);
		
		Ext.apply(store.getProxy(),{url: me.getBaseUrl()+'?' + encodedUrl});

	    store.load({
		    callback: function(records, operation, success) {
		        if (success)
		        {
			    	if (callbacks != null)
			    	{
			    		callbacks.success( records, callbacks.scope );
			    	}		        	
		        }else{
			    	if (callbacks != null)
			    	{
			    		callbacks.failure( records, callbacks.scope );
			    	}
		        }
		    },
		    scope: me
		});
    },
    
    getCaseloadById: function( personId, store, callbacks ){
    	var me=this;
    	var success = function( response, view ){
    		var r = Ext.decode(response.responseText);
    		store.removeAll();
	    	if (r.rows.length > 0)
	    	{
	    		store.loadData(r.rows);
	    	}
	    	if (callbacks != null)
	    	{
	    		callbacks.success( r, callbacks.scope );
	    	}	
	    };

	    var failure = function( response ){
	    	me.apiProperties.handleError( response );	    	
	    	if (callbacks != null)
	    	{
	    		callbacks.failure( response, callbacks.scope );
	    	}
	    };
	    
		me.apiProperties.makeRequest({
			url:me.getBaseUrlForIdCaseload(personId),
			method: 'GET',
			successFunc: success,
			failureFunc: failure,
			scope: me
		});
    }    
});