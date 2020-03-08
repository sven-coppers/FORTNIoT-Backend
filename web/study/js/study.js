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

$("#update_predictions").click(function() {
    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/config/predictions/update/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        refresh();
    });
});

function refresh() {
    this.refreshDevices();
    this.refreshRules();
    this.refreshStates();
    this.refreshConfig();
    this.refreshStudy();
    this.refreshPredictions();
}

$("#start_listening").click(function() { applyConfig(true); });
$("#stop_listening").click(function() { applyConfig(false); });
$("#start_predicting").click(function() { applyPredictions(true); });
$("#stop_predicting").click(function() { applyPredictions(false); });

function applyUseCase() {
    let useCase = {
        rule_set: [],
        device_set: [],
        state_set: []
    };

    $.each($("#rule_sets input"), function () {
        if($(this).prop('checked')) {
            useCase.rule_set.push($(this).attr("id").replace("rule_set_", ""));
        }
    });

    $.each($("#device_sets input"), function () {
        if($(this).prop('checked')) {
            useCase.device_set.push($(this).attr("id").replace("device_set_", ""));
        }
    });

    $.each($("#state_sets input"), function () {
        if($(this).prop('checked')) {
            useCase.state_set.push($(this).attr("id").replace("state_set_", ""));
        }
    });

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

function refreshPredictions() {
    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/config/predictions",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#start_predicting").attr('disabled', data["predictions"]);
        $("#stop_predicting").attr('disabled', !data["predictions"]);
    });
}

function refreshStudy() {
    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/study/case/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#rule_sets").empty();
        $("#device_sets").empty();
        $("#state_sets").empty();

        // Populate
        for(let rulSetOption of data["rule_set_options"].sort()) {
            $("#rule_sets").append('<div class="checkbox_option"><input type="checkbox" id="rule_set_' + rulSetOption + '" value="rule_set_' + rulSetOption + '"><label for="rule_set_' + rulSetOption + '">' + rulSetOption + '</label></div>');
        }

        for(let deviceSetOption of data["device_set_options"].sort()) {
            $("#device_sets").append('<div class="checkbox_option"><input type="checkbox" id="device_set_' + deviceSetOption + '" value="device_set_' + deviceSetOption + '"><label for="device_set_' + deviceSetOption + '">' + deviceSetOption + '</label></div>');
        }

        for(let stateSetOption of data["state_set_options"].sort()) {
            $("#state_sets").append('<div class="checkbox_option"><input type="checkbox" id="state_set_' + stateSetOption + '" value="state_set_' + stateSetOption + '"><label for="state_set_' + stateSetOption + '">' + stateSetOption + '</label></div>');
        }

        // Select
        for(let rulSetSelection of data["rule_set"]) {
            $('#rule_set_' + rulSetSelection).prop('checked', true);
        }
        for(let deviceSetSelection of data["device_set"]) {
            $('#device_set_' + deviceSetSelection).prop('checked', true);
        }
        for(let stateSetSelection of data["state_set"]) {
            $('#state_set_' + stateSetSelection).prop('checked', true);
        }
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

function applyPredictions(predictions) {
    let config = {predictions : predictions};

    $.ajax({
        url:            "http://localhost:8080/intelligibleIoT/api/config/predictions/",
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