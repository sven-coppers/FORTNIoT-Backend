$(document).ready(function() {
    refresh();
});

$(document).on('submit','form',function(){
    applyRuleProperties();
    return false;
});

function refresh() {
    this.refreshRules();

    const urlParams = new URLSearchParams(window.location.search);

    $("input[name=scenario][value=" + urlParams.get('scenario') + "]").attr('checked', 'checked');
    $("input[name=predictions][value=" + urlParams.get('predictions') + "]").attr('checked', 'checked');
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
                '    <td>' + HTMLID + '</td>\n' +
                '    <td>' + rule["description"] + '</td>\n' +
                '    <td id="' + HTMLID + '_actions"></td>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="rules_enabled" id="' + HTMLID + '_enabled" value="' + HTMLID + '_enabled"></td>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="rules_available" id="' + HTMLID + '_available" value="' + HTMLID + '_available"></td>\n' +
                '</tr>');

            setRuleProperties(HTMLID, rule["enabled"], rule["available"]);

            for(let action of rule["actions"]) {
                $("#" + HTMLID + "_actions").append(action["description"] + "<br />");
            }
        }
    });
}

function setRuleProperties(HTMLID, enabled, available) {
    $("#" + HTMLID + "_enabled").prop('checked', enabled);
    $("#" + HTMLID + "_available").prop('checked', available);
}

function ruleIDToHTML(ruleName) {
    return ruleName.replace("rule.", "");
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

function forceClientRefresh() {

}

function loadSimple() {
    setRuleProperties();
}