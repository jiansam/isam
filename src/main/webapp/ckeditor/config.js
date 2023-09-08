/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */
CKEDITOR.stylesSet.add( 'my_styles', [
    // Block-level styles.
    { name: 'Blue Title', element: 'h2', styles: { color: 'Blue' } },
    { name: 'Red Title',  element: 'h3', styles: { color: 'Red' } },
    { name: '藍色標註', element: 'div', styles: { 'font-family': '微軟正黑體, Calibri',color: '#003399' } },
    // Inline styles.
    { name: '內文', element: 'span', styles: { 'font-family': '微軟正黑體, Calibri' } }
]);
CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.stylesSet = 'my_styles';
	config.toolbar_Full = [    
	               	    { name: 'document', items : [ 'Source','Preview']},
	               	    { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
	               	    { name: 'editing', items : [ 'Find','Replace'] },
	               	    { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Subscript','Superscript','-','RemoveFormat' ] },
	               	 '/',
	               	    { name: 'links', items : [ 'Link'] },
	               	    { name: 'insert', items : [ 'Table' ] },
	               	    { name: 'tools', items : [ 'Maximize', 'ShowBlocks','-','About' ] } ,
	               	    { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote', '-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'] },
	               	 '/',
	               	    { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
	               	    { name: 'colors', items : [ 'TextColor','BGColor' ]}
	               	    ];
	config.toolbar = 'Full';
	config.font_names = '標楷體;微軟正黑體;Calibri;sans-serif;' +  CKEDITOR.config.font_names;
	config.pasteFromWordRemoveFontStyles = false;
	config.pasteFromWordRemoveStyles = false;
	config.title = false;
	config.allowedContent = true;
};