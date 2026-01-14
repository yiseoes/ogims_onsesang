
// /js/listVolOffer.js
(function () {
  function $(sel, root) { return (root || document).querySelector(sel); }
  function on(el, ev, fn) { if (el) el.addEventListener(ev, fn); }

  function getForm() {
    return $("#searchForm") || document.querySelector('form[action*="listVolOffer"]') || document.forms[0];
  }

  function setHidden(form, name, value) {
    var input = form.querySelector('input[name="' + name + '"]');
    if (!input) {
      input = document.createElement('input');
      input.type = 'hidden';
      input.name = name;
      form.appendChild(input);
    }
    input.value = value;
  }

  function submitWithResetPage() {
    var form = getForm();
    if (!form) return;
    // reset to first page when filters change
    setHidden(form, "page", "1");
    form.submit();
  }

   document.addEventListener("DOMContentLoaded", function () {
      var form = getForm();
      if (!form) return;

      // Auto-submit on "모집중만" checkbox change
      var onlyOpen = $('input[name="status"][type="checkbox"]', form);
      on(onlyOpen, "change", submitWithResetPage);

      // Auto-submit on category select change
      var category = $('#categorySelect', form) || $('select[name="category"]', form);
      on(category, "change", submitWithResetPage);

      // ✅ 여기 추가: submit 시 날짜를 searchKeywordHidden에 합치기
      on(form, "submit", function () {
        var cond = $('#searchCondition', form);
        if (cond && cond.value === "date") {
          var from = $('#dateFrom', form)?.value || "";
          var to   = $('#dateTo', form)?.value || "";
          var hidden = $('#searchKeywordHidden', form);
          if (hidden) {
            hidden.value = from && to ? (from + "~" + to) : (from || to);
          }
        }
      });
    });
  })();