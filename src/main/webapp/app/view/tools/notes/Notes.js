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
Ext.define('Ssp.view.tools.notes.Notes', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.personnotes',
    mixins: [ 'Deft.mixin.Injectable',
              'Deft.mixin.Controllable'],
   inject: {
       store: 'personNotesStore'
    },
    minHeight: '400',
    width: '98%',
    height: '100%',
    controller: 'Ssp.controller.tool.notes.NotesViewController',
    autoScroll: true,
	title: 'Notes and Communication to Student',
	renderDate: function(val, metaData, record) {
		    return Ext.util.Format.date( record.get('dateNoteTaken'),'m/d/Y');		
	},
	columnWrap: function(val, metaData, record){
	    return '<div style="white-space:normal !important;">'+ val +'</div>';
	},
    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            store: me.store,
            columns: [
				{
					xtype: 'gridcolumn',
				    dataIndex: 'dateNoteTaken',
				    text: 'Date',
				    flex: 0.08,
					renderer: me.renderDate
				},
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'noteType',
                    text: 'Type',
                    flex: 0.12
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'author',
                    text: 'Author',
                    flex: 0.12
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'department',
                    text: 'Department',
                    flex: 0.12
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'note',
                    text: 'Note',
					sortable: 'false',
                    flex: 0.50,
					renderer: me.columnWrap
                }
            ],
            viewConfig: {
                markDirty:false
            }
        });

        me.callParent(arguments);
    }
});