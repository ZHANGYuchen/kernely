AppHolidayManagerRequest = (function($){
	
	var mainView = null;
	var tableView1 = null;
	var tableView2 = null;
	var viewVisualize = null;
	var dates = new Array();
	var viewAccept =null;
	var viewDeny = null;
	
	HolidayManagerRequestPageView = Backbone.View.extend({
		el:"#request-manager-main",
		events: {
			
		},
	
		initialize: function(){
			viewAccept = new HolidayRequestAcceptView();
			viewDeny = new HolidayRequestDenyView();
			viewVisualize = new HolidayManagerVisualizeView();
		},
		
		render:function(){
			tableView1 = new HolidayManagerRequestPendingTableView().render();
			tableView2 = new HolidayManagerRequestTableView().render();
		}
	})

	
	HolidayManagerRequestPendingTableView = Backbone.View.extend({
		el:"#manager_pending_request_table",
		events:{
		
		},
		
		table: null,
		
		initialize:function(){
			var parent = this;
			
			var templateFromColumn = $("#from-column-template").text();
			var templateRequesterColumn = $("#requester-comment-column-template").text();
			var templateBeginColumn = $("#begin-column-template").text();
			var templateEndColumn = $("#end-column-template").text();
			this.table = $(parent.el).kernely_table({
				idField:"id",
				
				columns:[
						{"name":templateFromColumn, style:""},
						{"name":templateRequesterColumn, style:""}, 
						{"name":templateBeginColumn, style:""},
						{"name":templateEndColumn, style:""},
						{"name":"", style:["general-bg", "text-center", "no-border-right", "no-border-top", "no-border-bottom"]},
						{"name":"", style:["general-bg", "text-center", "no-border-right", "no-border-top", "no-border-bottom"]},
						{"name":"", style:["general-bg", "text-center", "no-border-right", "no-border-top", "no-border-bottom"]}
				],
				elements:["user", "requesterComment", "beginDateString", "endDateString", "acceptButton", "denyButton", "visualizeButton"],
				
				eventNames:["click", "click .accept", "click .deny", "click .visualize"],
				events:{
					"click": parent.selectLine,
					"click .accept" : parent.accept,
					"click .deny" : parent.deny,
					"click .visualize" : parent.visualize
					
				}
			});
		},
		
		accept: function(e){
			viewAccept.render(e.data.line);
		},
		
		deny: function(e){
			viewDeny.render(e.data.line);
		},
		
		visualize: function(e){
			viewVisualize.render(e.data.line);
		},
		
		selectLine : function(e){
			$(".editButton").removeAttr('disabled');
			$(".lockButton").removeAttr('disabled');
			var template = null;
			lineSelected = e.data.line;
		},
		
		reload: function(){
			this.render();
		},
		
		render: function(){
			var parent = this;
			$.ajax({
				type:"GET",
				url:"/holiday/managers/request/all/pending",
				dataType:"json",
				success: function(data){
					if(data != null){
						var dataRequest = data.holidayRequestDTO;
						if($.isArray(dataRequest)){

							$.each(dataRequest, function(){
								this.acceptButton = "<img class='accept clickable' src='/images/icons/rights_icon.png' />";
								this.denyButton = "<img class='deny clickable' src='/images/icons/delete_icon.png' />";
								this.visualizeButton = "<img class='visualize clickable' src='/images/icons/visualize_icon.png' />";
							});
						}
						else{
							dataRequest.acceptButton = "<img class='accept clickable' src='/images/icons/rights_icon.png' />";
							dataRequest.denyButton = "<img class='deny clickable' src='/images/icons/delete_icon.png' />";
							dataRequest.visualizeButton = "<img class='visualize clickable' src='/images/icons/visualize_icon.png' />";
						}
					}
					parent.table.reload(dataRequest);
				}
			});
			return this;
		}
	})
	
	HolidayManagerRequestTableView = Backbone.View.extend({
		el:"#manager_request_table",
		events:{
		
		},
		
		table: null, 
		initialize:function(){
			var parent = this;
			
			var templateFromColumn = $("#from-column-template").text();
			var templateRequesterColumn = $("#requester-comment-column-template").text();
			var templateManagerColumn = $("#manager-comment-column-template").text();
			var templateBeginColumn = $("#begin-column-template").text();
			var templateEndColumn = $("#end-column-template").text();
			this.table = $(parent.el).kernely_table({
				idField:"id",
				
				columns:[
						{"name":templateFromColumn, style:""},
						{"name":templateRequesterColumn, style:""},
						{"name":templateManagerColumn, style:""},
						{"name":templateBeginColumn, style:""},
						{"name":templateEndColumn, style:""},
						{"name":"", style:["general-bg", "text-center", "no-border-right", "no-border-top", "no-border-bottom"]}
				],
				elements:["user", "requesterComment", "managerComment", "beginDateString", "endDateString", "status"],
				
				eventNames:["click"],
				events:{
					"click": parent.selectLine
				}
			});
		},
		
		reload: function(){
			this.render();
		},
		
		render: function(){
			var parent = this;
			$.ajax({
				type:"GET",
				url:"/holiday/managers/request/all/status",
				dataType:"json",
				success: function(data){
					if(data != null){
						var dataRequest = data.holidayRequestDTO;
						if($.isArray(dataRequest)){
							$.each(dataRequest, function(){
								if(this.status == 1){
									this.status = "<img src='/images/icons/accept_icon.png' />";
								}
								else{
									this.status = "<img src='/images/icons/deny_icon.png' />";
								}
							});
						}
						else{
							if(dataRequest.status == 1){
								dataRequest.status = "<img src='/images/icons/accept_icon.png' />";
							}
							else{
								dataRequest.status = "<img src='/images/icons/deny_icon.png' />";
							}
						}
						parent.table.reload(dataRequest);
					}
				}
			});
			return this;
		}
	})
	
	HolidayRequestAcceptView = Backbone.View.extend({
		el: "#modal_accept_window_holiday_request",
		
		vid: null,
		
		events:{
			"click .validateHolidayRequest" : "accepted"
		},
		
		initialize:function(){
			var parent = this;
			var template = $("#popup-accept-template").html();
			var titleTemplate = $("#accept-template").html();
			$(this.el).kernely_dialog({
				title: titleTemplate,
				content: template,
				eventNames:'click',
				width:"395px"
			});
		},
		
		render : function(id){
			this.vid=id;
			$(this.el).kernely_dialog("open");
			return this;
		},
		
		accepted : function(){
			var parent = this;
			$.ajax({
				url : "/holiday/managers/request/accept/" + this.vid,
				success : function(){
					$.ajax({
						url : "/holiday/managers/request/comment/" + parent.vid,
						data : {comment: $("#comment_accept").val()},
						dataType: "json",
						success : function(){
							$(parent.el).kernely_dialog("close");
							var successHtml = $("#holiday-accept-template").html();				
							$.writeMessage("success",successHtml);
							tableView1.reload();
							tableView2.reload();
						}
					});
				}
			});
		}
	})

	HolidayRequestDenyView = Backbone.View.extend({
		el: "#modal_deny_window_holiday_request",

		vid: null,
		
		events:{
			"click .denyHolidayRequest" : "denied"
		},
		
		initialize:function(){
			var parent = this;
			var template = $("#popup-deny-template").html();
			var titleTemplate = $("#deny-template").html();
			$(this.el).kernely_dialog({
				title: titleTemplate,
				content: template,
				eventNames:'click',
				width:"395px"
			});
		},
		
		render : function(id){
			this.vid=id;
			$(this.el).kernely_dialog("open");
			return this;
		},
		
		closemodal: function(){
			$('#modal_window_holiday_request').hide();
	   		$('#mask').hide();
		},
		
		denied : function(){
			var parent = this ; 
			$.ajax({
				url : "/holiday/managers/request/deny/" + this.vid,
				success : function(){
					$.ajax({
						url : "/holiday/managers/request/comment/" + parent.vid,
						data : {comment: $("#comment_deny").val()},
						dataType: "json",
						success : function(){
							$(parent.el).kernely_dialog("close");
							var successHtml = $("#holiday-accept-template").html();				
							$.writeMessage("success",successHtml);
							tableView1.reload();
							tableView2.reload();
						}
					});
				}
			});
		}
	})
	
	HolidayManagerVisualizeView = Backbone.View.extend({
		el:"#modal_visualize_window_holiday_request",
		
		managerRequestView : null,
		vid : null,
		data : null,
		listDay : null,
		
		events:{
			"click #button_accepted" : "acceptModal",
			"click #button_denied" : "denyModal"			
		},
		
		initialize: function(){
			var parent = this;
			var template = $("#popup-visualize-template").html();
			var titleTemplate = $("#visualize-template").html();
			$(this.el).kernely_dialog({
				title: titleTemplate,
				content: template,
				eventNames:'click',
				width:"768px"
			});
		},
		
		render: function(id){
			this.vid=id;
			var parent =this;
			$.ajax({
				url : "/holiday/managers/request/details/"+ this.vid,
				dataType:"json",
				success : function(list){
					parent.listDay = list;	
					$.ajax({
						url : "/holiday/managers/request/get/"+ parent.vid,
						dataType:"json",
						success : function(data1){
							dates[0]=data1.holidayDetailDTO[0].day.substr(0,10);
							dates[1]=data1.holidayDetailDTO[1].day.substr(0,10);
							parent.data=data1;
							$.ajax({
								type: "GET",
								url:"/holiday/managers/request/construct",
								data:{dateBegin:dates[0], dateEnd:dates[1]},
								dataType:"json",
								success: function(data2){
									// Clean the div content
									$('#calendarContent').html("");
									// Create the views
									$("#calendarRequest").show();
									new HolidayRequestCalendarView(data2, parent.data, parent.listDay).render();
								}
							});
						}
					});
				}
			});
			$(this.el).kernely_dialog("open");
			return this;
		},
		
		acceptModal:function(){
			$(this.el).kernely_dialog("close");
			viewAccept.render(this.vid);
		},
		
		denyModal:function(){
			$(this.el).kernely_dialog("close");
			viewDeny.render(this.vid);
		}		
	})

	HolidayRequestCalendarView = Backbone.View.extend({
		el:"#calendarContent",
		data : null,
		details : null,
		listDays:null,
		
		events:{

		},
		
		initialize: function(data, details, listDays){
			this.data = data;
			this.details = details;
			this.listDays = listDays; 
		},
		
		
		render: function(){
			var parent = this;
			var view = null;
			
			// Variables declarations :
			// List of the headers : contains days
			var headerList = new Array();
			// List of the available mornings
			var morningList = new Array();
			// List of the available afternoons
			var afternoonList = new Array();
			
			var isColor = false;
			// Counter for the list building
			var cptBuildingList = 0;
			// Counter for the header list
			var cptHeaderList = 0;
			// Counter for the morning list
			var cptMorningList = 0;
			// Counter for the afternoon list
			var cptAfternoonList = 0;
			
			var cptDetailsList = 0;
			// Contains a tr element to add a row for header
			var lineHeader;
			// Contains a tr element to add a row for morning
			var lineMorning;
			// Contains a tr element to add a row for afternoon
			var lineAfternoon;
			// Count the number of weeks created
			var nPath = 0;
			

			// organise date
			var dateTake = new Array();
			var iDate = 0;
			
			if (this.listDays.holidayDetailDTO.length>1){
				$.each(this.listDays.holidayDetailDTO, function(){
					var year = this.day.substr(0,4);
					var month = this.day.substr(5,2);
					var day = this.day.substr(8,2);
					var dayDone = month+"/"+day+"/"+year;
					dateTake[iDate] = dayDone;
					iDate++;
				});
			}
			else {
				var year = this.listDays.holidayDetailDTO.day.substr(0,4);
				var month = this.listDays.holidayDetailDTO.day.substr(5,2);
				var day = this.listDays.holidayDetailDTO.day.substr(8,2);
				var dayDone = month+"/"+day+"/"+year;
				dateTake[iDate] = dayDone;
				iDate++;
			}
			
			// Building the header list
			// Building the morning list
			// Building the afternoon list
			if(this.data.days.length > 1){
				$.each(this.data.days, function(){
					headerList[cptBuildingList] = this.day;
					morningList[cptBuildingList] = "true";
					afternoonList[cptBuildingList] = "true";
					cptBuildingList ++;
				});
			}
			else{
				headerList[0] = this.data.days.day;
				morningList[0] = "true";
				afternoonList[0] = "true";
			}
			
			
			while (nPath < this.data.nbWeeks){
				// Create tr element
				lineHeader = $("<tr>", {
					class:'day-header'
				});
				// Adds all the headers for the week
				while(cptHeaderList < 5){
					lineHeader.append($(new HolidayRequestDayView(headerList[cptHeaderList + (nPath * 5)], true, true, false, -1).render().el));
					cptHeaderList ++;
				}
				$(parent.el).append(lineHeader);
				cptHeaderList = 0;
				
				// Create the tr element
				lineMorning = $("<tr>", {
					class:'morning-part'
				});
				
				// Adds all the mornings for the week
				while(cptMorningList < 5){
					lineMorning.append($(new HolidayRequestDayView(headerList[cptHeaderList + (nPath * 5)], morningList[cptMorningList + (nPath * 5)], false, true, false, dateTake, parent.listDays).render().el));
					cptMorningList ++;
					cptHeaderList++;
				}
				
				$(parent.el).append(lineMorning);
				cptHeaderList = 0;
				
				// Create the tr element
				lineAfternoon = $("<tr>", {
					class:'afternoon-part'
				});
				
				// Adds all the afternoons for the week
				while(cptAfternoonList < 5){
					lineAfternoon.append($(new HolidayRequestDayView(headerList[cptHeaderList + (nPath * 5)], afternoonList[cptAfternoonList + (nPath * 5)], false, false, true, dateTake, parent.listDays).render().el));
					cptAfternoonList ++;
					cptHeaderList ++;
				}
				$(parent.el).append(lineAfternoon);
				
				// Add an empty line for the separation of the week
				$(parent.el).append($("<tr>", {
					class:'separation-part'
				}));
				
				// Reinitialize all counters
				cptHeaderList = 0;
				cptMorningList = 0;
				cptAfternoonList = 0;
				// Increases week created
				nPath ++;
			}
			
			return this;
		}
	})
	
	 HolidayRequestDayView = Backbone.View.extend({
		tagName: "td",

		day : null,
		details:null,
		available : null,
		color : null,
		isHeader: false,
		// We just specify if morning, if this is false, and header too ,this is afternoon
		isMorning: false,
		isAfternoon: false,
		isColored : false,
		

		events:{
		},

		initialize: function(day, available, header, morning, afternoon, take, details){
			this.day = day;
			this.details = details;
			this.available = available;
			this.isHeader = header;
			this.isMorning = morning;
			this.isAfternoon = afternoon;
			for (xDate in take){
				if (this.details.holidayDetailDTO.length > 1){
					if (take[xDate]==this.day && this.isMorning.toString() == this.details.holidayDetailDTO[xDate].am
						|| take[xDate]==this.day && this.isAfternoon.toString() == this.details.holidayDetailDTO[xDate].pm){
						this.color=details.holidayDetailDTO[xDate].color;
						this.isColored = true ;
					}
				}
				else{
					if (take[xDate]==this.day && this.isMorning.toString() == this.details.holidayDetailDTO.am
						|| take[xDate]==this.day && this.isAfternoon.toString() == this.details.holidayDetailDTO.pm){
						this.color=details.holidayDetailDTO.color;
						this.isColored = true ;
					}
				}
			}
		},
		
			
		render: function(){
			if(this.isHeader){
				$(this.el).text(this.day);
				$(this.el).addClass("day-header-part");
			}
			else{
				if(this.available == "true"){
					if(this.isMorning){
						$(this.el).addClass("am-part");
						if (this.isColored == true && this.isHeader == false && this.isMorning == true){
							$(this.el).css('background-color', this.color);
						}
					}
					if(this.isAfternoon){
						$(this.el).addClass("pm-part");
						if (this.isColored == true && this.isHeader == false && this.isAfternoon == true){
							$(this.el).css('background-color', this.color);
						}
					}
				}
				else{
					$(this.el).attr('disabled', '');
					$(this.el).addClass('day-disabled');
				}
			}	
			return this;
		}

	})

	
	var self = {};
	self.start = function(){
		mainView = new HolidayManagerRequestPageView();
		mainView.render();
	}
	return self;
})

$(function() {
	console.log("Starting holiday manager request application")
	new AppHolidayManagerRequest(jQuery).start();
})