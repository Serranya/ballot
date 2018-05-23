"use strict";

const DragAndDrop = (function() {
	function initDragList(selectsContainerId, targetListId) {
		let targetList = document.getElementById(targetListId);
		const selectsContainer = document.getElementById(selectsContainerId);

		function renderList() {
			let isFirst = true;

			for (const select of selectsContainer.elements) {
				if (select.selectedOptions.length === 0) {
					continue;
				}

				const rank = parseInt(select.dataset.rank);

				let outerItem = document.createElement("li");
				outerItem.dataset.rank = rank;
				targetList.appendChild(outerItem);

				if (isFirst) {
					let beforeInnerList = createBefore();
					outerItem.appendChild(beforeInnerList);
					isFirst = false;
				}

				let innerList = createInnerList(select);
				outerItem.appendChild(innerList);

				let afterInnerList = createAfter();
				outerItem.appendChild(afterInnerList);
			}
		}

		function createBefore() {
			let before = document.createElement("div");
			before.classList.add("before");
			before.addEventListener("dragover", allowDrop);
			before.addEventListener("drop", drop);

			return before;
		}

		function createInnerList(select) {
			let list = document.createElement("ul");
			list.addEventListener("dragover", allowDrop);
			list.addEventListener("drop", drop);

			for (const option of select.selectedOptions) {
				let item = document.createElement("li");
				item.setAttribute("draggable", true);
				item.addEventListener("dragstart", drag);
				item.dataset.choiceId = option.value;
				item.appendChild(document.createTextNode(option.text));

				list.appendChild(item);
			}

			return list;
		}

		function createAfter() {
			let after = document.createElement("div");
			after.classList.add("after");
			after.addEventListener("dragover", allowDrop);
			after.addEventListener("drop", drop);

			return after;
		}

		function drag(ev) {
			const choiceId = ev.target.dataset.choiceId;
			ev.dataTransfer.effectAllowed = "move";

			let transferData = {
				"choiceId": choiceId,
				"sourceRank": parseInt(ev.target.parentElement.parentElement.dataset.rank),
				"sourceSelectContainerId": selectsContainer.id
			};

			ev.dataTransfer.setData("application/json", JSON.stringify(transferData));
		}

		function allowDrop(ev) {
			const transferData = JSON.parse(ev.dataTransfer.getData("application/json"));
			if (!(transferData.sourceSelectContainerId === selectsContainer.id)) {
				return;
			}
			ev.preventDefault();
			ev.dataTransfer.dropEffect = "move";
		}

		function drop(ev) {
			ev.preventDefault();

			const transferData = JSON.parse(ev.dataTransfer.getData("application/json"));
			const choiceId = transferData.choiceId;
			const sourceRank = transferData.sourceRank;
			let targetRank;

			if (ev.target.tagName.toLowerCase() === "li") {
				targetRank = parseInt(ev.target.parentElement.parentElement.dataset.rank);
				for (const select of selectsContainer.elements) {
					select[choiceId].selected = parseInt(select.dataset.rank) === targetRank;
				}
			} else {
				targetRank = parseInt(ev.target.parentElement.dataset.rank);
				if (targetRank === sourceRank && getSelectByRank(targetRank).selectedOptions.length === 1) {
					return;
				}
				for (const select of selectsContainer.elements) {
					select[choiceId].selected = false;
					if (parseInt(select.dataset.rank) === targetRank) {
						let dataName = ev.target.classList.contains("before") ? "before" : "after";
						select.dataset[dataName] = choiceId;
					}
				}
			}

			// process data-before and data-after attributes on select to reorganize them
			for (const select of selectsContainer.elements) {
				const selectRank = parseInt(select.dataset.rank);
				if (select.dataset.before) {
					moveDown(selectRank);

					select[choiceId].selected = true;

					delete select.dataset.before;
					break;
				} else if (select.dataset.after) {
					let realSelect;
					if (sourceRank < targetRank) {
						moveUp(selectRank);
						realSelect = getSelectByRank(selectRank);
					} else if (sourceRank => targetRank) {
						moveDown(selectRank + 1);
						realSelect = getSelectByRank(selectRank + 1);
					}

					realSelect[choiceId].selected = true

					delete select.dataset.after;
					break;
				}
			}

			reRenderList();
		}

		function moveDown(fromRank) {
			let toMove;
			for (const select of selectsContainer.elements) {
				if (parseInt(select.dataset.rank) < fromRank) continue;

				if (toMove) {
					if (toMove.length === 0) break;
					let oldOptions = [];
					Array.from(select.selectedOptions).forEach(o => {
						oldOptions.push(o.value);
						o.selected = false;
					});
					for (const value of toMove) {
						select.options[value].selected = true;
					}
					toMove = oldOptions;
				} else {
					toMove = [];
					Array.from(select.selectedOptions).forEach(o => {
						toMove.push(o.value);
						o.selected = false;
					});
				}
			}
		}

		function moveUp(fromRank) {
			let toMove;
			for (let i = (selectsContainer.elements.length - 1); i >= 0; i--) {
				const select = selectsContainer.elements.item(i);
				if (parseInt(select.dataset.rank) > fromRank) continue;
				if (toMove) {
					if (toMove.length === 0) break;
					let oldOptions = [];
					Array.from(select.selectedOptions).forEach(o => {
						oldOptions.push(o.value);
						o.selected = false;
					});
					for (const value of toMove) {
						select.options[value].selected = true;
					}
					toMove = oldOptions;
				} else {
					toMove = [];
					Array.from(select.selectedOptions).forEach(o => {
						toMove.push(o.value);
						o.selected = false;
					});
				}
			}
		}

		function getSelectByRank(rank) {
			for (const select of selectsContainer.elements) {
				if (parseInt(select.dataset.rank) === rank) return select;
			}
		}

		function reRenderList() {
			var cTargetList = targetList.cloneNode(false);
			targetList.parentNode.replaceChild(cTargetList, targetList);
			targetList = cTargetList;
			renderList();
		}

		// sort and safe size so we can use an index to access the correct rank
		// This seems stupid and unnecessary now..
		let selects = [].slice.call(selectsContainer.elements);
		selects.sort(function(a, b) {
			let aRank = parseInt(a.dataset.rank);
			let bRank = parseInt(b.dataset.rank);

			return aRank - bRank;
		});
		selects.forEach(function(val, idx) { // might not be needed
			selectsContainer.appendChild(val);
		});

		renderList();
	}

	return {
		"initDragList": initDragList
	};
})();
