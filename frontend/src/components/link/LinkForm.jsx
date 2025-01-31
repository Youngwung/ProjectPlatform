import React from "react";

const LinkForm = ({ newLink, setNewLink, editLink, setEditLink, handleCreateLink, handleUpdateLink, linkTypes }) => {
  return (
    <div>
      <h2>{editLink ? "링크 수정" : "새 링크 생성"}</h2>
      <input
        type="text"
        placeholder="URL"
        value={editLink ? editLink.url : newLink.url}
        onChange={(e) =>
          editLink
            ? setEditLink({ ...editLink, url: e.target.value })
            : setNewLink({ ...newLink, url: e.target.value })
        }
      />
      <input
        type="text"
        placeholder="설명"
        value={editLink ? editLink.description : newLink.description}
        onChange={(e) =>
          editLink
            ? setEditLink({ ...editLink, description: e.target.value })
            : setNewLink({ ...newLink, description: e.target.value })
        }
      />
      <select
        value={editLink ? editLink.linkTypeId : newLink.linkTypeId}
        onChange={(e) =>
          editLink
            ? setEditLink({ ...editLink, linkTypeId: Number(e.target.value) })
            : setNewLink({ ...newLink, linkTypeId: Number(e.target.value) })
        }
      >
        <option value="">-- 링크 타입 선택 --</option>
        {linkTypes.map((type) => (
          <option key={type.id} value={type.id}>
            {type.name}
          </option>
        ))}
      </select>

      {editLink ? (
        <>
          <button onClick={handleUpdateLink}>저장</button>
          <button onClick={() => setEditLink(null)}>취소</button>
        </>
      ) : (
        <button onClick={handleCreateLink}>생성</button>
      )}
    </div>
  );
};

export default LinkForm;
