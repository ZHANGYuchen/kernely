<script type="text/javascript" src="/js/streams.js"></script>
<script type="text/html" id="message-template">
<div id="mess-{{id}}" class='mess'>
	<div class="mess-left-side">
		<div class="message-image">
			<img class="message-img-image" src='/img/picture.png'/>
		</div>
	</div>
	<div class="mess-right-side">
		<div  class='message-content'>
			<div class="message-buttons" id="buttons{{id}}">
			 <img id="delete{{id}}" class="deleteButton" src="/img/delete.png"/>
			</div>
			{{message}}
		</div>
		<div class="message-author-info">
			<span class="info-message"><%= i18n.t("posted_by") %></span>
		</div>
		<div class="comment-field">
			<div id="comments-{{id}}"></div>
			<div id="other_comment{{id}}" class="other-comment"><span class="loadcomment"><%= i18n.t("view_comments") %><span/></div>
			<div id="input_comment{{id}}" class="input-comment"><input type="text" value='<%= i18n.t("comment_here") %>' class="input-comment-field-dis" /></div>
		</div>
	</div>
	<div style="clear:both;"></div>	
</div>
</script>

<script type="text/html" id="view-comments-template">
<span class='loadcomment'><%= i18n.t("view_comments") %><span/>
</script>

<script type="text/html" id="hide-comments-template">
<span class='hidecomment'><%= i18n.t("hide_comments") %><span/>
</script>

<script type="text/html" id="confirm-deletion-template">
<%= i18n.t("confirm_message_deletion") %>
</script>

<script type="text/html" id="confirm-deletion-comment-template">
<%= i18n.t("confirm_comment_deletion") %>
</script>

<script type="text/html" id="alert-empty-message-template">
<%= i18n.t("empty_message_forbidden") %>
</script>

<script type="text/html" id="comment-here-template">
<input type="text" value='<%= i18n.t("comment_here") %>' class="input-comment-field-dis"/>
</script>

<script type="text/html" id="comment-template">
<div id="comment-{{id}}" class='comment'>
	<div class="comment-left-side">
		<div class="comment-image">
			<img class="comment-img-image" src='{{commentPicture}}'/>
		</div>
	</div>
	<div class="comment-right-side">
		<div class='comment-content'>
			<div class="comment-buttons" id="comm_buttons{{id}}">
				<img id="delete{{id}}" class="deleteCommentButton" src="/img/delete.png"/>
			</div>
			{{comment}}
		</div>
		<div class="comment-author-info">
			<span class="info-comment"><%= i18n.t("comment_posted_by") %></span>
		</div>
	</div>
	<div style="clear:both;"></div>
</div>
</script>

<script type="text/html" id="input_comment-template">
<textarea class='comment-input' id='{{commentInputId}}'></textarea>
<a class='cancelButton'><%= i18n.t("cancel") %></a>  <a class='button share-comment' href='javascript:void(0)' ><%= i18n.t("comment_message") %></a>
</script>

<!-- Dates -->
<script type="text/html" id="date-seconds-template">
<%= i18n.t("stream_seconds_message") %>
</script>
<script type="text/html" id="date-minutes-template">
<%= i18n.t("stream_minutes_message") %>
</script>
<script type="text/html" id="date-hours-template">
<%= i18n.t("stream_hours_message") %>
</script>
<script type="text/html" id="date-days-template">
<%= i18n.t("stream_days_message") %>
</script>

<div id="streams">
	<div id="streams-main">
		<h1><%= i18n.t("stream_page_title") %></h1>
		<div>
			<textarea id="message-input"></textarea>		
			<div class="button-bar" id="combo">
				<a id="share-message"  class="button share-message" href="javascript:void(0)" ><%= i18n.t("share_button") %></a>
			</div>
		</div>
		<div id="streams-messages">
		</div>
	</div>
	<div id="streams-sidebar">
	</div>
	<div id="streams-footer">
	</div>
</div>