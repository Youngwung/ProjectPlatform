import React, { useEffect, useState } from "react";
import linkApi from "../api/linkApi";
import LinkList from "../components/link/LinkList";
import LinkForm from "../components/link/LinkForm";
import { Container } from "react-bootstrap";

const Link = () => {
  const [links, setLinks] = useState([]);
  const [linkTypes, setLinkTypes] = useState([]);
  const [newLink, setNewLink] = useState({ userId: 1, url: "", description: "", linkTypeId: "" });
  const [editLink, setEditLink] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchLinks();
    fetchLinkTypes();
  }, []);

  const fetchLinks = async () => {
    try {
      const data = await linkApi.getAllLinks();
      setLinks(data);
    } catch (err) {
      setError("링크 목록을 불러오는 데 실패했습니다.");
    }
  };

  const fetchLinkTypes = async () => {
    try {
      const data = await linkApi.getAllLinkTypes();
      setLinkTypes(data);
    } catch (err) {
      setError("링크 타입을 불러오는 데 실패했습니다.");
    }
  };

  const handleCreateLink = async () => {
    if (!newLink.linkTypeId) return setError("링크 타입을 선택하세요.");
    await linkApi.createLink(newLink);
    setNewLink({ userId: 1, url: "", description: "", linkTypeId: "" });
    fetchLinks();
  };

  const handleUpdateLink = async () => {
    await linkApi.updateLink(editLink.id, editLink);
    setEditLink(null);
    fetchLinks();
  };

  const handleDeleteLink = async (id) => {
    await linkApi.deleteLink(id);
    fetchLinks();
  };

  return (
    <Container>
      <h1 className="mt-4">Link 관리</h1>
      {error && <p className="text-danger">{error}</p>}
      <LinkForm {...{ newLink, setNewLink, editLink, setEditLink, handleCreateLink, handleUpdateLink, linkTypes }} />
      <LinkList {...{ links, linkTypes, setEditLink, handleDeleteLink }} />
    </Container>
  );
};

export default Link;