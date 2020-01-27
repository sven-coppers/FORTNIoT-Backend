$(document).ready(function() {
    refresh();
});

$(document).on('submit','form',function(){
    //applyRuleProperties();
    applyUseCase();
    return false;
});


$("#apply").click(function() {
    applyUseCase();
});

function refresh() {
    this.refreshDevices();
    this.refreshRules();
    this.refreshStates();
    this.refreshConfig();
}

$("#start_listening").click(function() { applyConfig(true); });
$("#stop_listening").click(function() { applyConfig(false); });


function applyUseCase() {
    let useCase = {
        rule_set: $( "#rule_set option:selected" ).text(),
        device_set: $( "#device_set option:selected" ).text(),
        state_set: $( "#state_set option:selected" ).text(),
    };

    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/study/case/",
        type:           "PUT",
        data: JSON.stringify(useCase),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
    }).done(function (data) {
        refresh();
    });
}

function refreshRules() {
    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/rules/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#table_body_rules").empty();

        for (let ruleID in data) {
            let rule = data[ruleID];
            let HTMLID = ruleIDToHTML(ruleID);

            $("#table_body_rules").append('<tr>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="rules_enabled" id="' + HTMLID + '_enabled" value="' + HTMLID + '_enabled"></td>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="rules_available" id="' + HTMLID + '_available" value="' + HTMLID + '_available"></td>\n' +
                '    <td>' + HTMLID + '</td>\n' +
                '    <td>' + rule["description"] + '</td>\n' +
                '    <td id="' + HTMLID + '_actions"></td>\n' +
                '</tr>');

            setRuleProperties(HTMLID, rule["enabled"], rule["available"]);

            for(let action of rule["actions"]) {
                $("#" + HTMLID + "_actions").append(action["description"] + "<br />");
            }
        }
    });
}

function refreshStates() {
    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/states/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#table_body_states").empty();

        for (let deviceID in data) {
            let deviceState = data[deviceID];

            if(deviceState == null) continue;

            $("#table_body_states").append('<tr>\n' +
                '    <td>' + deviceID + '</td>\n' +
                '    <td>' + deviceState["state"] + '</td>\n' +
                '    <td>' + dateToString(new Date(deviceState["last_changed"])) + '</td>\n' +
                '    <td>' + dateToString(new Date(deviceState["last_updated"]))+ '</td>\n' +
                //     '    <td>' + JSON.stringify(device["attributes"]).substring(0, 100) + '...</td>\n' +
                '</tr>');

            //  setRuleProperties(HTMLID, rule["enabled"], rule["available"]);
        }
    });
}

function refreshDevices() {
    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/bram/devices/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#table_body_devices").empty();

        for (let device of data) {
            if(device == null) continue;
            let HTMLID = deviceIDtoHTMLID(device["id"]);

            $("#table_body_devices").append('<tr>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="device_enabled" id="' + HTMLID + '_enabled" value="' + HTMLID + '_enabled"></td>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="device_available" id="' + HTMLID + '_available" value="' + HTMLID + '_available"></td>\n' +
                '    <td>' + device["id"] + '</td>\n' +
                '    <td>' + device["friendly_name"] + '</td>\n' +
            '</tr>');

            setDeviceProperties(HTMLID, device["enabled"], device["available"]);
        }
    });
}

function refreshConfig() {
    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/config/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#start_listening").attr('disabled', data["connected_to_hassio"]);
        $("#stop_listening").attr('disabled', !data["connected_to_hassio"]);
    });
}

function setRuleProperties(HTMLID, enabled, available) {
    $("#" + HTMLID + "_enabled").prop('checked', enabled);
    $("#" + HTMLID + "_available").prop('checked', available);
}

function setDeviceProperties(HTMLID, enabled, available) {
    $("#" + HTMLID + "_enabled").prop('checked', enabled);
    $("#" + HTMLID + "_available").prop('checked', available);
}

function ruleIDToHTML(ruleName) {
    return ruleName.replace("rule.", "");
}

function deviceIDtoHTMLID(deviceID) {
    return deviceID.replace(".", "_"); // Only the first occurrence
}

function HTMLIDtoDeviveID(HTMLID) {
    return deviceID.replace("_", "."); // Only the first occurrence
}

function applyRuleProperties() {
    let ruleProperties = {};

    $.each($("#table_body_rules tr"), function () {
        let HTMLID = $(this).children("td").first().text();
        let enabled = $("#" + HTMLID + "_enabled").prop('checked');
        let available = $("#" + HTMLID + "_available").prop('checked');

        ruleProperties["rule." + HTMLID] = { enabled: enabled, available: available };
    });

    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/rules/",
        type:           "PUT",
        data: JSON.stringify(ruleProperties),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
    }).done(function (data) {
        refresh();
    });
}

function applyConfig(listeningToConfig) {
    let config = {connected_to_hassio : listeningToConfig};

    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/config/",
        type:           "PUT",
        data: JSON.stringify(config),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
    }).done(function (data) {
        refresh();
    });
}

function dateToString(date) {
    return date.getDate() + "/" + date.getMonth() + "/" + date.getFullYear() + " " + timeToString(date);
}

function timeToString(date) {
    let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    let minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();

    return hours + ":" + minutes;
}